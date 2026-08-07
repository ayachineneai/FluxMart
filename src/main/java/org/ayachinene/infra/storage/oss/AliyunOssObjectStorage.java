package org.ayachinene.infra.storage.oss;

import com.aliyun.oss.OSS;
import org.ayachinene.app.storage.ObjectStorage;
import org.springframework.stereotype.Component;

@Component
public class AliyunOssObjectStorage implements ObjectStorage {

    private final OSS ossClient;
    private final OssProperties properties;

    public AliyunOssObjectStorage(
            OSS ossClient,
            OssProperties properties
    ) {
        this.ossClient = ossClient;
        this.properties = properties;
    }

    @Override
    public boolean exists(String objectKey) {
        return ossClient.doesObjectExist(
                properties.bucket(),
                objectKey
        );
    }
}
