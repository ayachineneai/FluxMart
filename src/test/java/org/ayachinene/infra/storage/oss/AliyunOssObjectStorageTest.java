package org.ayachinene.infra.storage.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PolicyConditions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AliyunOssObjectStorageTest {

    @Test
    void readsObjectMetadataFromTheConfiguredBucket() {
        var ossClient = mock(OSS.class);
        var properties = new OssProperties(
                "https://oss-cn-example.aliyuncs.com",
                "cn-example",
                "fluxmart",
                "accessKeyId",
                "accessKeySecret"
        );
        var storage = new AliyunOssObjectStorage(ossClient, properties);
        var metadata = new ObjectMetadata();
        metadata.setContentType("image/png");
        metadata.setContentLength(1024L);
        when(ossClient.getObjectMetadata("fluxmart", "products/image.png"))
                .thenReturn(metadata);

        var storedObject = storage.find("products/image.png").orElseThrow();

        assertEquals("image/png", storedObject.contentType());
        assertEquals(1024L, storedObject.sizeInBytes());
        verify(ossClient).getObjectMetadata("fluxmart", "products/image.png");
    }

    @Test
    void createsRestrictedV4PostUploadAuthorization() {
        var ossClient = mock(OSS.class);
        var properties = new OssProperties(
                "https://oss-cn-example.aliyuncs.com",
                "cn-example",
                "fluxmart",
                "accessKeyId",
                "accessKeySecret"
        );
        var storage = new AliyunOssObjectStorage(ossClient, properties);
        var expiresAt = OffsetDateTime.parse("2026-08-08T12:05:00+08:00");
        var policy = "{\"expiration\":\"2026-08-08T04:05:00.000Z\"}";
        when(ossClient.generatePostPolicy(
                eq(Date.from(expiresAt.toInstant())),
                any(PolicyConditions.class)
        )).thenReturn(policy);
        when(ossClient.calculatePostSignature(
                eq(policy),
                any(Date.class)
        )).thenReturn("signed-policy");

        var authorization = storage.authorizeUpload(
                "product-images/file-id",
                "image/png",
                1024L,
                expiresAt
        );

        assertEquals(
                "https://fluxmart.oss-cn-example.aliyuncs.com",
                authorization.url()
        );
        assertEquals("product-images/file-id", authorization.fields().key());
        assertEquals("image/png", authorization.fields().contentType());
        assertEquals("201", authorization.fields().successActionStatus());
        assertEquals("true", authorization.fields().forbidOverwrite());
        assertEquals("OSS4-HMAC-SHA256",
                authorization.fields().signatureVersion());
        assertEquals("signed-policy", authorization.fields().signature());
        assertEquals(
                Base64.getEncoder().encodeToString(
                        policy.getBytes(StandardCharsets.UTF_8)
                ),
                authorization.fields().policy()
        );
        assertTrue(authorization.fields().credential()
                .contains("/cn-example/oss/aliyun_v4_request"));

        var conditionsCaptor = ArgumentCaptor.forClass(PolicyConditions.class);
        verify(ossClient).generatePostPolicy(
                eq(Date.from(expiresAt.toInstant())),
                conditionsCaptor.capture()
        );
        var conditions = conditionsCaptor.getValue().jsonize();
        assertTrue(conditions.contains("\"bucket\":\"fluxmart\""));
        assertTrue(conditions.contains("product-images\\/file-id"));
        assertTrue(conditions.contains("image\\/png"));
        assertTrue(conditions.contains("[\"content-length-range\",1024,1024]"));
    }
}
