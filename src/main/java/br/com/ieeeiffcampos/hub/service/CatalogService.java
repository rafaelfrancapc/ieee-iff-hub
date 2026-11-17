package br.com.ieeeiffcampos.hub.service;

import br.com.ieeeiffcampos.hub.config.CatalogProperties;
import br.com.ieeeiffcampos.hub.domain.Chapter;
import br.com.ieeeiffcampos.hub.domain.HubEntry;
import br.com.ieeeiffcampos.hub.domain.SearchResult;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CatalogService {
    private static final Pattern DIACRITICS = Pattern.compile("\\p{M}+");
    private final CatalogProperties catalog;

    public CatalogService(CatalogProperties catalog) { this.catalog = catalog; }
    public List<HubEntry> services() { return catalog.services(); }
    public List<HubEntry> documents() { return catalog.documents(); }
    public List<Chapter> chapters() { return catalog.chapters(); }

    public HubEntry entry(String collection, String slug) {
        return entries(collection).stream().filter(entry -> entry.slug().equals(slug)).findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Chapter chapter(String slug) {
        return chapters().stream().filter(chapter -> chapter.slug().equals(slug)).findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<HubEntry> entries(String collection) {
        return switch (collection) {
            case "servicos" -> services();
            case "documentos" -> documents();
            default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        };
    }

    public List<HubEntry> filter(String collection, String query, String category) {
        return entries(collection).stream()
            .filter(entry -> category.isBlank() || entry.category().equals(category))
            .filter(entry -> matches(query, entry.title(), entry.summary(), entry.category()))
            .toList();
    }

    public List<SearchResult> search(String query, String type) {
        if (query.isBlank()) return List.of();
        var results = new ArrayList<SearchResult>();
        for (String collection : List.of("servicos", "documentos")) {
            if (!type.isBlank() && !type.equals(collection)) continue;
            for (HubEntry entry : filter(collection, query, "")) {
                results.add(new SearchResult(entry.title(), entry.summary(), collection,
                    "/" + collection + "/" + entry.slug(), entry.state()));
            }
        }
        if (type.isBlank() || type.equals("capitulos")) {
            chapters().stream().filter(chapter -> matches(query, chapter.acronym(), chapter.name())).forEach(chapter -> results.add(new SearchResult(
                    chapter.acronym() + ": " + chapter.name(), "Diretoria e contatos do capítulo.", "capitulos",
                    "/capitulos/" + chapter.slug(), chapter.state())));
        }
        return List.copyOf(results);
    }

    public List<String> categories(String collection) {
        return entries(collection).stream().map(HubEntry::category).distinct().toList();
    }

    public static String cleanQuery(String value) {
        if (value == null) return "";
        String result = value.strip();
        return result.substring(0, Math.min(result.length(), 200));
    }

    private static boolean matches(String query, String... fields) {
        String haystack = normalize(String.join(" ", fields));
        return Pattern.compile("\\s+").splitAsStream(normalize(query)).allMatch(haystack::contains);
    }

    private static String normalize(String value) {
        return DIACRITICS.matcher(Normalizer.normalize(value, Normalizer.Form.NFD))
            .replaceAll("").toLowerCase(Locale.ROOT);
    }
}
