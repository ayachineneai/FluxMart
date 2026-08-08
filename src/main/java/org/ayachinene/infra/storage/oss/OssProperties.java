package org.ayachinene.infra.storage.oss;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oss")
public record OssProperties(
        String endpoint,
        String region,
        String bucket,
        String accessKeyId,
        String accessKeySecret
) {
}
