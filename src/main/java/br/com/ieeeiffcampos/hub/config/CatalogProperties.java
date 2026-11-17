package br.com.ieeeiffcampos.hub.config;

import br.com.ieeeiffcampos.hub.domain.Chapter;
import br.com.ieeeiffcampos.hub.domain.HubEntry;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("catalogo")
public record CatalogProperties(List<HubEntry> services, List<HubEntry> documents, List<Chapter> chapters) {
    public CatalogProperties {
        services = services == null ? List.of() : List.copyOf(services);
        documents = documents == null ? List.of() : List.copyOf(documents);
        chapters = chapters == null ? List.of() : List.copyOf(chapters);
    }
}
