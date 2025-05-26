package br.com.backendpro.ratelimiter.application.dto;

import java.util.List;

public class IdentifierDTO {
    List<String> identifiers;

    public List<String> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<String> identifiers) {
        this.identifiers = identifiers;
    }
}
