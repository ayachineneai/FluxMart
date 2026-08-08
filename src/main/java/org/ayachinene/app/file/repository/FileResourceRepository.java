package org.ayachinene.app.file.repository;

import org.ayachinene.app.file.domain.FileResource;
import org.ayachinene.app.file.domain.FileStatus;
import org.ayachinene.shared.uuid7.UUID7;

import java.time.OffsetDateTime;

public interface FileResourceRepository {

    void create(
            FileResource file,
            OffsetDateTime uploadExpiresAt
    );

    FileResource find(UUID7 fileId);

    boolean updateStatus(
            UUID7 fileId,
            FileStatus expectedStatus,
            FileStatus status
    );
}
