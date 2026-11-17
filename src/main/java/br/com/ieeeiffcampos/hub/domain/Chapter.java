package br.com.ieeeiffcampos.hub.domain;

import java.util.List;

public record Chapter(String slug, String acronym, String name, String state, String logo, List<Officer> officers) {
    public Chapter {
        officers = officers == null ? List.of() : List.copyOf(officers);
    }

    public record Officer(String role, String name) { }
}
