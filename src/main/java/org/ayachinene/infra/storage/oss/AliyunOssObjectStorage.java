package org.ayachinene.infra.storage.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PolicyConditions;
import org.ayachinene.app.file.storage.ObjectStorage;
import org.ayachinene.app.file.storage.StoredObject;
import org.ayachinene.app.file.storage.UploadAuthorization;
import org.ayachinene.app.file.storage.UploadFormFields;
import org.ayachinene.utils.Base64s;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@Component
public class AliyunOssObjectStorage implements ObjectStorage {

    private static final String SIGNATURE_VERSION = "OSS4-HMAC-SHA256";
    private static final String SUCCESS_STATUS = "201";
    private static final DateTimeFormatter SIGNING_DATE =
            DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneOffset.UTC);
    private static final DateTimeFormatter SIGNING_DATE_TIME =
            DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")
                    .withZone(ZoneOffset.UTC);

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
    public UploadAuthorization authorizeUpload(
            String objectKey,
            String contentType,
            long sizeInBytes,
            OffsetDateTime expiresAt
    ) {
        var signingInstant = Instant.now();
        var credential = credential(signingInstant);
        var signingDateTime = SIGNING_DATE_TIME.format(signingInstant);

        var policy = ossClient.generatePostPolicy(
                Date.from(expiresAt.toInstant()),
            uploadConditions(
                    objectKey,
                    contentType,
                    sizeInBytes,
                    credential,
                    signingDateTime
            )
        );
        var signature = ossClient.calculatePostSignature(
                policy,
                Date.from(signingInstant)
        );

        return new UploadAuthorization(
                uploadUrl(),
                new UploadFormFields(
                        objectKey,
                        contentType,
                        SUCCESS_STATUS,
                        "true",
                        Base64s.encode(policy),
                        SIGNATURE_VERSION,
                        credential,
                        signingDateTime,
                        signature
                )
        );
    }

    private PolicyConditions uploadConditions(
            String objectKey,
            String contentType,
            long sizeInBytes,
            String credential,
            String signingDateTime
    ) {
        var conditions = new PolicyConditions();
        conditions.addConditionItem("bucket", properties.bucket());
        conditions.addConditionItem(PolicyConditions.COND_KEY, objectKey);
        conditions.addConditionItem(
                PolicyConditions.COND_CONTENT_TYPE,
                contentType
        );
        conditions.addConditionItem(
                PolicyConditions.COND_CONTENT_LENGTH_RANGE,
                sizeInBytes,
                sizeInBytes
        );
        conditions.addConditionItem(
                PolicyConditions.COND_SUCCESS_ACTION_STATUS,
                SUCCESS_STATUS
        );
        conditions.addConditionItem("x-oss-forbid-overwrite", "true");
        conditions.addConditionItem(
                "x-oss-signature-version",
                SIGNATURE_VERSION
        );
        conditions.addConditionItem("x-oss-credential", credential);
        conditions.addConditionItem("x-oss-date", signingDateTime);
        return conditions;
    }

    private String credential(Instant signingInstant) {
        return "%s/%s/%s/oss/aliyun_v4_request".formatted(
                properties.accessKeyId(),
                SIGNING_DATE.format(signingInstant),
                properties.region()
        );
    }

    private String uploadUrl() {
        var endpointUri = URI.create(properties.endpoint());
        return endpointUri.getScheme()
                + "://"
                + properties.bucket()
                + "."
                + endpointUri.getRawAuthority();
    }

    @Override
    public Optional<StoredObject> find(String objectKey) {
        try {
            var metadata = ossClient.getObjectMetadata(
                    properties.bucket(),
                    objectKey
            );
            return Optional.of(new StoredObject(
                    metadata.getContentType(),
                    metadata.getContentLength()
            ));
        } catch (OSSException exception) {
            if ("NoSuchKey".equals(exception.getErrorCode())) {
                return Optional.empty();
            }
            throw exception;
        }
    }
}
