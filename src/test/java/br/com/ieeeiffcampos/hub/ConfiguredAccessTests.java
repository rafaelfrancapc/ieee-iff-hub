package br.com.ieeeiffcampos.hub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
    "hub.repositories-url=https://git.example.org/ieee",
    "hub.account-url=https://id.example.org/self-service",
    "hub.inventory-url=https://patrimonio.example.org"
})
@WebAppConfiguration
class ConfiguredAccessTests {
    @Autowired WebApplicationContext context;
    MockMvc mvc;

    @BeforeEach
    void setup() { mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build(); }

    @Test
    void repositoryAccessUsesOnlyTheConfiguredDestination() throws Exception {
        mvc.perform(get("/repositorios").param("url", "https://untrusted.example"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("https://git.example.org/ieee"));
    }

    @Test
    void inventoryUsesTheConfiguredServiceAndKeepsAuthenticationOutsideTheHub() throws Exception {
        mvc.perform(get("/acessar/patrimonio")).andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("https://patrimonio.example.org"));
        mvc.perform(get("/servicos/patrimonio")).andExpect(status().isOk())
            .andExpect(content().string(containsString("https://patrimonio.example.org")))
            .andExpect(content().string(org.hamcrest.Matchers.not(containsString("type=\"password\""))));
    }

    @Test
    void accountServiceLinksToSelfServiceWithoutALocalPasswordForm() throws Exception {
        mvc.perform(get("/acessar/conta")).andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("https://id.example.org/self-service"));
        mvc.perform(get("/servicos/conta")).andExpect(status().isOk())
            .andExpect(content().string(containsString("https://id.example.org/self-service")))
            .andExpect(content().string(containsString("Gerenciar minha conta")))
            .andExpect(content().string(org.hamcrest.Matchers.not(containsString("type=\"password\""))));
    }
}
