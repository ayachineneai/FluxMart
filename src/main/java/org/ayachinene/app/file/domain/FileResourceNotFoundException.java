package org.ayachinene.app.file.domain;

import org.ayachinene.shared.uuid7.UUID7;

public class FileResourceNotFoundException extends RuntimeException {

    public FileResourceNotFoundException(UUID7 fileId) {
        super("File resource not found: " + fileId);
    }
}
