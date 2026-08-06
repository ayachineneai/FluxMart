package org.ayachinene.app.domain.file;

import org.ayachinene.app.uuid7.UUID7;

import java.util.Objects;

public record FileResourceId(UUID7 value) {
    public FileResourceId {
        Objects.requireNonNull(value, "value must not be null");
    }
}
