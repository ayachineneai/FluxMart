package org.ayachinene.app.file.storage;

public record StoredObject(
        String contentType,
        long sizeInBytes
) {
}
