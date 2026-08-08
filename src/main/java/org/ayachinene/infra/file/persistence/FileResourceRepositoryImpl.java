package org.ayachinene.infra.file.persistence;

import org.ayachinene.app.file.domain.FileResource;
import org.ayachinene.app.file.domain.FileResourceNotFoundException;
import org.ayachinene.app.file.domain.FileStatus;
import org.ayachinene.app.file.repository.FileResourceRepository;
import org.ayachinene.shared.uuid7.UUID7;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Repository
public class FileResourceRepositoryImpl implements FileResourceRepository {

    private final FileResourceMapper fileMapper;
    private final FilePersistenceConverter persistenceConverter;

    public FileResourceRepositoryImpl(
            FileResourceMapper fileMapper,
            FilePersistenceConverter persistenceConverter
    ) {
        this.fileMapper = fileMapper;
        this.persistenceConverter = persistenceConverter;
    }

    @Override
    public void create(
            FileResource file,
            OffsetDateTime uploadExpiresAt
    ) {
        var createdAt = LocalDateTime.now();
        var filePo = persistenceConverter.toFileResourcePo(file)
                .setUploadExpiresAt(uploadExpiresAt.toLocalDateTime())
                .setCreatedAt(createdAt)
                .setUpdatedAt(createdAt);
        fileMapper.insert(filePo);
    }

    @Override
    public FileResource find(UUID7 fileId) {
        var filePo = fileMapper.selectById(fileId);
        if (filePo == null) {
            throw new FileResourceNotFoundException(fileId);
        }
        return persistenceConverter.toFileResource(filePo);
    }

    @Override
    public boolean updateStatus(
            UUID7 fileId,
            FileStatus expectedStatus,
            FileStatus status
    ) {
        return fileMapper.updateStatus(
                fileId,
                expectedStatus,
                status,
                LocalDateTime.now()
        ) == 1;
    }
}
