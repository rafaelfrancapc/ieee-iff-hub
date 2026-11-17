package br.com.ieeeiffcampos.hub;

import br.com.ieeeiffcampos.hub.config.HubProperties;
import br.com.ieeeiffcampos.hub.service.DocumentService;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ConfigurationTests {
    @Test
    void externalAddressesRejectScriptSchemesAndEmbeddedCredentials() {
        assertThatThrownBy(() -> new HubProperties("javascript:alert(1)", "", "", "", "", "", "")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new HubProperties("https://user:password@example.org", "", "", "", "", "", "")).isInstanceOf(IllegalArgumentException.class);
        assertThat(new HubProperties("https://cloud.example.org", "", "", "", "", "", "").cloudConfigured()).isTrue();
    }

    @Test
    void documentRendererEscapesRawHtmlAndDangerousLinks() {
        String html = new DocumentService().render("unsafe-test.md");
        assertThat(html).doesNotContain("<script>").doesNotContain("href=\"javascript:").contains("&lt;script&gt;");
        assertThatThrownBy(() -> new DocumentService().render("../application.yml")).isInstanceOf(IllegalArgumentException.class);
    }
}
