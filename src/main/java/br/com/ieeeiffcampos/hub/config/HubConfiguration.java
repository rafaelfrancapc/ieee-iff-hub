package br.com.ieeeiffcampos.hub.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({HubProperties.class, CatalogProperties.class})
public class HubConfiguration {
}
