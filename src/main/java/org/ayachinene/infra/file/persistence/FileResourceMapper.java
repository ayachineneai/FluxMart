package org.ayachinene.infra.file.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.ayachinene.app.file.domain.FileStatus;
import org.ayachinene.shared.uuid7.UUID7;

import java.time.LocalDateTime;

@Mapper
public interface FileResourceMapper extends BaseMapper<FileResourcePO> {

    default int updateStatus(
            UUID7 fileId,
            FileStatus expectedStatus,
            FileStatus status,
            LocalDateTime updatedAt
    ) {
        return update(
                Wrappers.<FileResourcePO>lambdaUpdate()
                        .set(FileResourcePO::getStatus, status)
                        .set(FileResourcePO::getUpdatedAt, updatedAt)
                        .eq(FileResourcePO::getId, fileId)
                        .eq(FileResourcePO::getStatus, expectedStatus)
        );
    }
}
