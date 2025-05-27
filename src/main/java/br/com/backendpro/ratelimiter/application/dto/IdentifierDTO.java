package br.com.backendpro.ratelimiter.application.dto;

import java.util.List;
import java.util.Objects;

public class IdentifierDTO {
    List<String> identifiers;

    public IdentifierDTO(List<String> identifiers) {
        this.identifiers = identifiers;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IdentifierDTO that = (IdentifierDTO) o;
        return Objects.equals(identifiers, that.identifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(identifiers);
    }
}
