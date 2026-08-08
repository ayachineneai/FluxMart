package org.ayachinene.app.file.storage;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface ObjectStorage {

    UploadAuthorization authorizeUpload(
            String objectKey,
            String contentType,
            long sizeInBytes,
            OffsetDateTime expiresAt
    );

    Optional<StoredObject> find(String objectKey);

}
