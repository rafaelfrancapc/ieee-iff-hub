package br.com.ieeeiffcampos.hub.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {
    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().escapeHtml(true).sanitizeUrls(true).build();
    private final Map<String, String> rendered = new ConcurrentHashMap<>();

    public String render(String filename) {
        if (filename == null || filename.isBlank()) return "";
        if (!filename.matches("[a-z0-9-]+\\.md")) throw new IllegalArgumentException("Documento inválido.");
        return rendered.computeIfAbsent(filename, this::read);
    }

    private String read(String filename) {
        try (var input = new ClassPathResource("content/" + filename).getInputStream()) {
            return renderer.render(parser.parse(new String(input.readAllBytes(), StandardCharsets.UTF_8)));
        } catch (IOException exception) {
            throw new IllegalStateException("Não foi possível ler o documento " + filename, exception);
        }
    }
}
