package br.com.ieeeiffcampos.hub.web;

import br.com.ieeeiffcampos.hub.config.HubProperties;
import br.com.ieeeiffcampos.hub.domain.HubEntry;
import br.com.ieeeiffcampos.hub.service.CatalogService;
import br.com.ieeeiffcampos.hub.service.DocumentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HubController {
    private final CatalogService catalog;
    private final DocumentService documents;
    private final HubProperties properties;

    public HubController(CatalogService catalog, DocumentService documents, HubProperties properties) {
        this.catalog = catalog;
        this.documents = documents;
        this.properties = properties;
    }

    @ModelAttribute("hub")
    HubProperties properties() { return properties; }

    @GetMapping("/")
    String home(Model model) {
        model.addAttribute("pageTitle", "Início");
        model.addAttribute("activePage", "inicio");
        model.addAttribute("chapters", catalog.chapters());
        model.addAttribute("documents", catalog.documents().stream().limit(3).toList());
        model.addAttribute("services", catalog.services());
        return "home";
    }

    @GetMapping("/{collection:servicos|documentos}")
    String listing(@PathVariable String collection,
                   @RequestParam(defaultValue = "") String q,
                   @RequestParam(defaultValue = "") String categoria,
                   Model model) {
        String title = switch (collection) {
            case "servicos" -> "Serviços";
            case "documentos" -> "Documentação";
            default -> "Documentação";
        };
        String description = switch (collection) {
            case "servicos" -> "Ferramentas e espaços de trabalho do Ramo.";
            default -> "Guias de acesso e referências públicas.";
        };
        model.addAttribute("collection", collection);
        model.addAttribute("pageTitle", title);
        model.addAttribute("activePage", collection);
        model.addAttribute("description", description);
        model.addAttribute("q", CatalogService.cleanQuery(q));
        model.addAttribute("selectedCategory", categoria);
        model.addAttribute("entries", catalog.filter(collection, CatalogService.cleanQuery(q), categoria));
        model.addAttribute("categories", catalog.categories(collection));
        model.addAttribute("chapters", catalog.chapters());
        return "listing";
    }

    @GetMapping("/{collection:servicos|documentos}/{slug}")
    String detail(@PathVariable String collection, @PathVariable String slug, Model model) {
        HubEntry entry = catalog.entry(collection, slug);
        model.addAttribute("pageTitle", entry.title());
        model.addAttribute("activePage", collection);
        model.addAttribute("collection", collection);
        model.addAttribute("entry", entry);
        model.addAttribute("body", documents.render(entry.content()));
        model.addAttribute("externalUrl", entry.linkKey().isEmpty() ? entry.externalUrl() : properties.serviceUrl(entry.linkKey()));
        return "detail";
    }

    @GetMapping("/capitulos")
    String chapters(Model model) {
        model.addAttribute("pageTitle", "Capítulos");
        model.addAttribute("activePage", "capitulos");
        model.addAttribute("chapters", catalog.chapters());
        return "chapters";
    }

    @GetMapping("/capitulos/{slug}")
    String chapter(@PathVariable String slug, Model model) {
        var chapter = catalog.chapter(slug);
        model.addAttribute("pageTitle", chapter.acronym());
        model.addAttribute("activePage", "capitulos");
        model.addAttribute("chapter", chapter);
        return "chapter";
    }

    @GetMapping({"/repositorios", "/projetos", "/projetos/{slug}"})
    String repositories() {
        String url = properties.repositoriesUrl();
        return "redirect:" + (url.isEmpty() ? "/servicos/repositorios" : url);
    }

    @GetMapping("/acessar/{slug}")
    String accessService(@PathVariable String slug) {
        HubEntry entry = catalog.entry("servicos", slug);
        String url = entry.linkKey().isEmpty() ? entry.externalUrl() : properties.serviceUrl(entry.linkKey());
        return "redirect:" + (url.isEmpty() ? "/servicos/" + entry.slug() : url);
    }

    @GetMapping("/busca")
    String search(@RequestParam(defaultValue = "") String q,
                  @RequestParam(defaultValue = "") String tipo, Model model) {
        String query = CatalogService.cleanQuery(q);
        model.addAttribute("pageTitle", "Busca");
        model.addAttribute("activePage", "busca");
        model.addAttribute("q", query);
        model.addAttribute("selectedType", tipo);
        model.addAttribute("results", catalog.search(query, tipo));
        return "search";
    }

    @GetMapping("/ajuda")
    String help(Model model) {
        model.addAttribute("pageTitle", "Ajuda");
        model.addAttribute("activePage", "ajuda");
        return "help";
    }
}
