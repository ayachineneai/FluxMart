package org.ayachinene.infra.storage.oss;

import com.aliyun.oss.OSS;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AliyunOssObjectStorageTest {

    @Test
    void checksWhetherAnObjectExistsInTheConfiguredBucket() {
        var ossClient = mock(OSS.class);
        var properties = new OssProperties(
                "https://oss-cn-example.aliyuncs.com",
                "fluxmart",
                "accessKeyId",
                "accessKeySecret"
        );
        var storage = new AliyunOssObjectStorage(ossClient, properties);
        when(ossClient.doesObjectExist("fluxmart", "products/image.jpg"))
                .thenReturn(true);
        when(ossClient.doesObjectExist("fluxmart", "products/missing.jpg"))
                .thenReturn(false);

        assertTrue(storage.exists("products/image.jpg"));
        assertFalse(storage.exists("products/missing.jpg"));
        verify(ossClient).doesObjectExist("fluxmart", "products/image.jpg");
        verify(ossClient).doesObjectExist("fluxmart", "products/missing.jpg");
    }
}
