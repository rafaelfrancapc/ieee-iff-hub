package br.com.ieeeiffcampos.hub.domain;

public record HubEntry(String slug, String title, String summary, String category,
                       String state, String access, String source, String content,
                       String linkKey, String externalUrl) {
    public HubEntry {
        content = content == null ? "" : content;
        externalUrl = externalUrl == null ? "" : externalUrl;
        linkKey = linkKey == null ? "" : linkKey;
    }
}
