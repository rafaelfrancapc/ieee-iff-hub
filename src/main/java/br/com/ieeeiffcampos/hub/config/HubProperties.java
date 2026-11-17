package br.com.ieeeiffcampos.hub.config;

import java.net.URI;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("hub")
public record HubProperties(String nextcloudUrl, String websiteUrl, String supportEmail,
                            String repositoriesUrl, String accountUrl, String internalDocsUrl, String inventoryUrl) {
    public HubProperties {
        nextcloudUrl = validateUrl(nextcloudUrl);
        websiteUrl = validateUrl(websiteUrl);
        repositoriesUrl = validateUrl(repositoriesUrl);
        accountUrl = validateUrl(accountUrl);
        internalDocsUrl = validateUrl(internalDocsUrl);
        inventoryUrl = validateUrl(inventoryUrl);
        supportEmail = supportEmail == null ? "" : supportEmail.trim();
        if (!supportEmail.isEmpty() && !supportEmail.matches("[^\\s@<>]+@[^\\s@<>]+\\.[^\\s@<>]+")) {
            throw new IllegalArgumentException("HUB_SUPPORT_EMAIL deve conter um endereço de e-mail válido.");
        }
    }

    private static String validateUrl(String value) {
        if (value == null || value.isBlank()) return "";
        URI uri = URI.create(value.trim());
        if (!("https".equals(uri.getScheme()) || "http".equals(uri.getScheme()))
                || uri.getHost() == null || uri.getUserInfo() != null) {
            throw new IllegalArgumentException("Os endereços do HUB devem usar HTTP(S), sem credenciais na URL.");
        }
        return uri.toString();
    }

    public boolean cloudConfigured() { return !nextcloudUrl.isEmpty(); }
    public boolean websiteConfigured() { return !websiteUrl.isEmpty(); }
    public boolean supportConfigured() { return !supportEmail.isEmpty(); }
    public String serviceUrl(String key) {
        return switch (key) {
            case "nextcloud" -> nextcloudUrl;
            case "repositories" -> repositoriesUrl;
            case "account" -> accountUrl;
            case "internal-docs" -> internalDocsUrl;
            case "inventory" -> inventoryUrl;
            default -> "";
        };
    }
    public boolean serviceConfigured(String key) { return !serviceUrl(key).isEmpty(); }
}
