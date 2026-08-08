package org.ayachinene.infra.file.persistence;

import org.ayachinene.app.file.domain.FilePurpose;
import org.ayachinene.app.file.domain.FileResource;
import org.ayachinene.app.file.domain.FileStatus;
import org.ayachinene.shared.uuid7.UUID7s;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FileResourceRepositoryImplTest {

    @Test
    void insertsUploadingFileResource() {
        var fileMapper = mock(FileResourceMapper.class);
        var converter = mock(FilePersistenceConverter.class);
        var file = new FileResource(
                UUID7s.generate(),
                "product-images/object",
                "black-shirt.png",
                "image/png",
                1024L,
                FilePurpose.PRODUCT_IMAGE,
                FileStatus.UPLOADING
        );
        var filePo = new FileResourcePO()
                .setId(file.fileId())
                .setObjectKey(file.objectKey())
                .setOriginalName(file.originalFilename())
                .setContentType(file.contentType())
                .setSize(file.sizeInBytes())
                .setPurpose(file.purpose())
                .setStatus(file.status());
        when(converter.toFileResourcePo(file)).thenReturn(filePo);
        var repository = new FileResourceRepositoryImpl(fileMapper, converter);
        var expiresAt = OffsetDateTime.parse("2026-08-08T12:05:00+08:00");

        repository.create(file, expiresAt);

        var poCaptor = ArgumentCaptor.forClass(FileResourcePO.class);
        verify(fileMapper).insert(poCaptor.capture());
        var inserted = poCaptor.getValue();
        assertEquals(file.fileId(), inserted.getId());
        assertEquals(expiresAt.toLocalDateTime(), inserted.getUploadExpiresAt());
        assertNotNull(inserted.getCreatedAt());
        assertEquals(inserted.getCreatedAt(), inserted.getUpdatedAt());
    }

    @Test
    void updatesUploadingFileStatusToAvailable() {
        var fileMapper = mock(FileResourceMapper.class);
        var converter = mock(FilePersistenceConverter.class);
        var repository = new FileResourceRepositoryImpl(fileMapper, converter);
        var fileId = UUID7s.generate();
        when(fileMapper.updateStatus(
                org.mockito.ArgumentMatchers.eq(fileId),
                org.mockito.ArgumentMatchers.eq(FileStatus.UPLOADING),
                org.mockito.ArgumentMatchers.eq(FileStatus.AVAILABLE),
                org.mockito.ArgumentMatchers.any()
        )).thenReturn(1);

        var updated = repository.updateStatus(
                fileId,
                FileStatus.UPLOADING,
                FileStatus.AVAILABLE
        );

        assertEquals(true, updated);
    }
}
