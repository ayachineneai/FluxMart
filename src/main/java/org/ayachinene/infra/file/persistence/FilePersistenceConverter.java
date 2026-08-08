package org.ayachinene.infra.file.persistence;

import org.ayachinene.app.file.domain.FileResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface FilePersistenceConverter {

    @Mapping(target = "id", source = "fileId")
    @Mapping(target = "originalName", source = "originalFilename")
    @Mapping(target = "size", source = "sizeInBytes")
    @Mapping(target = "uploadExpiresAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    FileResourcePO toFileResourcePo(FileResource file);

    @Mapping(target = "fileId", source = "id")
    @Mapping(target = "originalFilename", source = "originalName")
    @Mapping(target = "sizeInBytes", source = "size")
    FileResource toFileResource(FileResourcePO filePo);
}
