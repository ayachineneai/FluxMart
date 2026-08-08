package org.ayachinene.app.file;

import org.ayachinene.app.file.domain.Files;
import org.ayachinene.app.file.domain.FileStatus;
import org.ayachinene.app.file.domain.FileUploadCannotBeConfirmedException;
import org.ayachinene.app.file.repository.FileResourceRepository;
import org.ayachinene.app.file.storage.ObjectStorage;
import org.ayachinene.app.file.upload.PrepareProductImageUploadInput;
import org.ayachinene.app.file.upload.PreparedProductImageUpload;
import org.ayachinene.app.file.upload.ConfirmedFileUpload;
import org.ayachinene.shared.uuid7.UUID7;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class FileService {

    private static final long UPLOAD_AUTHORIZATION_MINUTES = 5L;

    private final FileResourceRepository fileRepository;
    private final ObjectStorage objectStorage;

    public FileService(
            FileResourceRepository fileRepository,
            ObjectStorage objectStorage
    ) {
        this.fileRepository = fileRepository;
        this.objectStorage = objectStorage;
    }

    public PreparedProductImageUpload prepareProductImageUpload(
            PrepareProductImageUploadInput input
    ) {
        var file = Files.createProductImageResource(input);
        var expiresAt = OffsetDateTime.now()
                .plusMinutes(UPLOAD_AUTHORIZATION_MINUTES);
        var authorization = objectStorage.authorizeUpload(
                file.objectKey(),
                file.contentType(),
                file.sizeInBytes(),
                expiresAt
        );

        fileRepository.create(file, expiresAt);

        return new PreparedProductImageUpload(
                file.fileId(),
                file.status(),
                authorization,
                expiresAt
        );
    }

    public ConfirmedFileUpload zconfirmProductImageUpload(UUID7 fileId) {
        var file = fileRepository.find(fileId);
        if (file.status() == FileStatus.AVAILABLE) {
            return confirmedUpload(fileId);
        }

        var storedObject = objectStorage.find(file.objectKey())
                .orElseThrow(() -> new FileUploadCannotBeConfirmedException(
                        fileId,
                        "uploaded object does not exist"
                ));
        var confirmedFile = Files.confirmUpload(file, storedObject);
        var updated = fileRepository.updateStatus(
                fileId,
                FileStatus.UPLOADING,
                confirmedFile.status()
        );
        if (!updated && fileRepository.find(fileId).status() != FileStatus.AVAILABLE) {
            throw new FileUploadCannotBeConfirmedException(
                    fileId,
                    "status changed concurrently"
            );
        }
        return confirmedUpload(fileId);
    }

    private ConfirmedFileUpload confirmedUpload(UUID7 fileId) {
        return new ConfirmedFileUpload(fileId, FileStatus.AVAILABLE);
    }
}
