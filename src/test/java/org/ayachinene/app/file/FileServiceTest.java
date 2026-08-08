package org.ayachinene.app.file;

import org.ayachinene.app.exception.ValidationException;
import org.ayachinene.app.file.domain.FileResource;
import org.ayachinene.app.file.domain.FileStatus;
import org.ayachinene.app.file.repository.FileResourceRepository;
import org.ayachinene.app.file.storage.ObjectStorage;
import org.ayachinene.app.file.storage.UploadAuthorization;
import org.ayachinene.app.file.storage.UploadFormFields;
import org.ayachinene.app.file.storage.StoredObject;
import org.ayachinene.app.file.upload.PrepareProductImageUploadInput;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

class FileServiceTest {

    @Test
    void validatesAuthorizesAndSavesProductImageUpload() {
        var fileRepository = mock(FileResourceRepository.class);
        var objectStorage = mock(ObjectStorage.class);
        var authorization = new UploadAuthorization(
                "https://fluxmart.oss-cn-shanghai.aliyuncs.com",
                new UploadFormFields(
                        "product-images/file-id",
                        "image/png",
                        "201",
                        "true",
                        "policy-value",
                        "OSS4-HMAC-SHA256",
                        "credential",
                        "20260808T040000Z",
                        "signature"
                )
        );
        when(objectStorage.authorizeUpload(any(), any(), any(Long.class), any()))
                .thenReturn(authorization);
        var service = new FileService(fileRepository, objectStorage);

        var result = service.prepareProductImageUpload(
                new PrepareProductImageUploadInput(
                        "  black-shirt.png  ",
                        "  IMAGE/PNG  ",
                        183421L
                )
        );

        var fileCaptor = ArgumentCaptor.forClass(FileResource.class);
        var expiresAtCaptor = ArgumentCaptor.forClass(OffsetDateTime.class);
        verify(fileRepository).create(
                fileCaptor.capture(),
                expiresAtCaptor.capture()
        );
        var savedFile = fileCaptor.getValue();
        assertEquals("black-shirt.png", savedFile.originalFilename());
        assertEquals("image/png", savedFile.contentType());
        assertEquals(FileStatus.UPLOADING, savedFile.status());
        assertEquals(savedFile.fileId(), result.fileId());
        assertEquals(authorization, result.authorization());
        assertEquals(expiresAtCaptor.getValue(), result.expiresAt());
    }

    @Test
    void rejectsInvalidInputBeforeIo() {
        var fileRepository = mock(FileResourceRepository.class);
        var objectStorage = mock(ObjectStorage.class);
        var service = new FileService(fileRepository, objectStorage);

        assertThrows(
                ValidationException.class,
                () -> service.prepareProductImageUpload(
                        new PrepareProductImageUploadInput(
                                "black-shirt.gif",
                                "image/gif",
                                1024L
                        )
                )
        );
        verifyNoInteractions(fileRepository, objectStorage);
    }

    @Test
    void confirmsUploadedObjectAndMakesFileAvailable() {
        var fileRepository = mock(FileResourceRepository.class);
        var objectStorage = mock(ObjectStorage.class);
        var fileId = org.ayachinene.shared.uuid7.UUID7s.generate();
        var file = new FileResource(
                fileId,
                "product-images/" + fileId,
                "black-shirt.png",
                "image/png",
                183421L,
                org.ayachinene.app.file.domain.FilePurpose.PRODUCT_IMAGE,
                FileStatus.UPLOADING
        );
        when(fileRepository.find(fileId)).thenReturn(file);
        when(objectStorage.find(file.objectKey()))
                .thenReturn(Optional.of(new StoredObject("image/png", 183421L)));
        when(fileRepository.updateStatus(
                fileId,
                FileStatus.UPLOADING,
                FileStatus.AVAILABLE
        )).thenReturn(true);
        var service = new FileService(fileRepository, objectStorage);

        var result = service.confirmProductImageUpload(fileId);

        assertEquals(fileId, result.fileId());
        assertEquals(FileStatus.AVAILABLE, result.status());
        verify(objectStorage).find(file.objectKey());
        verify(fileRepository).updateStatus(
                fileId,
                FileStatus.UPLOADING,
                FileStatus.AVAILABLE
        );
    }

    @Test
    void treatsAlreadyAvailableFileAsSuccessfullyConfirmed() {
        var fileRepository = mock(FileResourceRepository.class);
        var objectStorage = mock(ObjectStorage.class);
        var fileId = org.ayachinene.shared.uuid7.UUID7s.generate();
        when(fileRepository.find(fileId)).thenReturn(new FileResource(
                fileId,
                "product-images/" + fileId,
                "black-shirt.png",
                "image/png",
                183421L,
                org.ayachinene.app.file.domain.FilePurpose.PRODUCT_IMAGE,
                FileStatus.AVAILABLE
        ));
        var service = new FileService(fileRepository, objectStorage);

        var result = service.confirmProductImageUpload(fileId);

        assertEquals(FileStatus.AVAILABLE, result.status());
        verifyNoInteractions(objectStorage);
        verify(fileRepository, never()).updateStatus(any(), any(), any());
    }
}
