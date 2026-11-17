package br.com.ieeeiffcampos.hub;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import br.com.ieeeiffcampos.hub.config.HubProperties;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HubApplicationTests {
	@Autowired HubProperties properties;

	@Test
	void contextLoads() {
	}

	@Test
	void plannedServiceAddressesAreDefaults() {
		assertThat(properties.serviceUrl("nextcloud")).isEqualTo("https://nuvem.ieeeiffcampos.com.br");
		assertThat(properties.serviceUrl("repositories")).isEqualTo("https://gitlab.ieeeiffcampos.com.br");
		assertThat(properties.serviceUrl("account")).isEqualTo("https://sso.ieeeiffcampos.com.br");
	}

}
