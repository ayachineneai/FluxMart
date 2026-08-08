package org.ayachinene.infra.file.persistence;

import com.baomidou.mybatisplus.annotation.TableName;
import org.ayachinene.app.file.domain.FilePurpose;
import org.ayachinene.app.file.domain.FileStatus;
import org.ayachinene.shared.uuid7.UUID7;

import java.time.LocalDateTime;

@TableName("file_resource")
public class FileResourcePO {

    private UUID7 id;
    private String objectKey;
    private String originalName;
    private String contentType;
    private Long size;
    private FilePurpose purpose;
    private FileStatus status;
    private LocalDateTime uploadExpiresAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UUID7 getId() {
        return id;
    }

    public FileResourcePO setId(UUID7 id) {
        this.id = id;
        return this;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public FileResourcePO setObjectKey(String objectKey) {
        this.objectKey = objectKey;
        return this;
    }

    public String getOriginalName() {
        return originalName;
    }

    public FileResourcePO setOriginalName(String originalName) {
        this.originalName = originalName;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public FileResourcePO setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public Long getSize() {
        return size;
    }

    public FileResourcePO setSize(Long size) {
        this.size = size;
        return this;
    }

    public FilePurpose getPurpose() {
        return purpose;
    }

    public FileResourcePO setPurpose(FilePurpose purpose) {
        this.purpose = purpose;
        return this;
    }

    public FileStatus getStatus() {
        return status;
    }

    public FileResourcePO setStatus(FileStatus status) {
        this.status = status;
        return this;
    }

    public LocalDateTime getUploadExpiresAt() {
        return uploadExpiresAt;
    }

    public FileResourcePO setUploadExpiresAt(LocalDateTime uploadExpiresAt) {
        this.uploadExpiresAt = uploadExpiresAt;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public FileResourcePO setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public FileResourcePO setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
