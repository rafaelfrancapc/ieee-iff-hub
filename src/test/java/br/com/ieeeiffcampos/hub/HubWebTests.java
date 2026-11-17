package br.com.ieeeiffcampos.hub;

import br.com.ieeeiffcampos.hub.service.CatalogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"hub.nextcloud-url=", "hub.repositories-url=", "hub.account-url="})
@WebAppConfiguration
class HubWebTests {
    @Autowired WebApplicationContext context;
    @Autowired CatalogService catalog;
    @Autowired org.springframework.web.servlet.resource.ResourceUrlProvider resourceUrls;
    MockMvc mvc;

    @BeforeEach
    void setup() { mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build(); }

    @ParameterizedTest
    @ValueSource(strings = {"/", "/servicos", "/documentos", "/servicos/conta", "/capitulos", "/ajuda", "/busca"})
    void primaryPagesRender(String path) throws Exception {
        mvc.perform(get(path)).andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(content().string(containsString("lang=\"pt-BR\"")))
            .andExpect(content().string(containsString("/assets/theme-init.js")));
    }

    @Test
    void everyCatalogDetailAndChapterRenders() throws Exception {
        for (String collection : new String[]{"servicos", "documentos"}) {
            for (var entry : catalog.entries(collection)) {
                mvc.perform(get("/" + collection + "/" + entry.slug())).andExpect(status().isOk())
                    .andExpect(content().string(containsString(entry.title())));
            }
        }
        for (var chapter : catalog.chapters()) {
            mvc.perform(get("/capitulos/" + chapter.slug())).andExpect(status().isOk())
                .andExpect(content().string(containsString(org.springframework.web.util.HtmlUtils.htmlEscape(chapter.name()))));
        }
    }

    @Test
    void searchFindsAccentedContentAndCanBeScoped() throws Exception {
        mvc.perform(get("/busca").param("q", "recuperacao").param("tipo", "documentos"))
            .andExpect(status().isOk()).andExpect(model().attribute("results", org.hamcrest.Matchers.hasSize(1)))
            .andExpect(content().string(containsString("Conta e recuperação de acesso")));
    }

    @Test
    void reflectedSearchIsEscaped() throws Exception {
        mvc.perform(get("/busca").param("q", "<script>alert(1)</script>"))
            .andExpect(status().isOk()).andExpect(content().string(containsString("&lt;script&gt;")))
            .andExpect(content().string(not(containsString("<script>alert(1)</script>"))));
    }

    @Test
    void filtersIntersectAndEmptyStateProvidesRecovery() throws Exception {
        mvc.perform(get("/documentos").param("q", "recuperacao").param("categoria", "Acesso"))
            .andExpect(status().isOk()).andExpect(model().attribute("entries", org.hamcrest.Matchers.hasSize(1)));
        mvc.perform(get("/documentos").param("q", "termo-inexistente-xyz"))
            .andExpect(status().isOk()).andExpect(content().string(containsString("Nenhum recurso encontrado")))
            .andExpect(content().string(containsString("Ver catálogo completo")));
    }

    @Test
    void unconfiguredCloudDoesNotPretendToBeAvailable() throws Exception {
        mvc.perform(get("/acessar/nuvem")).andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/servicos/nuvem"));
        mvc.perform(get("/servicos/nuvem")).andExpect(status().isOk())
            .andExpect(content().string(containsString("Acesso em preparação")))
            .andExpect(content().string(containsString("disabled")));
    }

    @Test
    void missingResourcesReturn404() throws Exception {
        mvc.perform(get("/documentos/inexistente")).andExpect(status().isNotFound());
        mvc.perform(get("/capitulos/inexistente")).andExpect(status().isNotFound());
    }

    @Test
    void securityHeadersAndHealthAreBounded() throws Exception {
        mvc.perform(get("/")).andExpect(header().string("X-Content-Type-Options", "nosniff"))
            .andExpect(header().string("Content-Security-Policy", containsString("frame-ancestors 'none'")))
            .andExpect(header().doesNotExist("Set-Cookie"));
        mvc.perform(get("/actuator/health")).andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"))
            .andExpect(jsonPath("$.components").doesNotExist());
        mvc.perform(get("/actuator/env")).andExpect(status().isNotFound());
        mvc.perform(post("/servicos")).andExpect(status().isForbidden());
    }

    @Test
    void stylesheetHasAContentVersionAndTheVersionedUrlIsServed() throws Exception {
        String path = resourceUrls.getForLookupPath("/assets/hub.css");
        org.assertj.core.api.Assertions.assertThat(path).matches("/assets/hub-[a-f0-9]+\\.css");
        mvc.perform(get(path)).andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith("text/css"));
    }

    @Test
    void formerProjectPagesRedirectToTheRepositoryService() throws Exception {
        for (String path : new String[]{"/projetos", "/projetos/automacao-b26", "/repositorios"}) {
            mvc.perform(get(path)).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servicos/repositorios"));
        }
        mvc.perform(get("/")).andExpect(content().string(not(containsString("Projetos e iniciativas"))))
            .andExpect(content().string(not(containsString("Automação e maquetes"))))
            .andExpect(content().string(containsString("/assets/brand/ramo.webp")));
        mvc.perform(get("/busca").param("q", "automacao"))
            .andExpect(model().attribute("results", org.hamcrest.Matchers.empty()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ramo.webp", "cs.png", "embs.png", "pes.png", "ras.webp"})
    void suppliedLogosAreServed(String filename) throws Exception {
        mvc.perform(get("/assets/brand/" + filename)).andExpect(status().isOk());
    }
}
