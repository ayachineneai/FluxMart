package org.ayachinene.api.file.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * <pre>{@code
 * {
 *   "fileId": "0195d7d2-6380-7a5c-8b35-3a23b8df1f01",
 *   "status": "UPLOADING",
 *   "upload": {
 *     "url": "https://fluxmart.oss-cn-shanghai.aliyuncs.com",
 *     "fields": {
 *       "key": "product-images/2026/08/0195d7d2-6380-7a5c-8b35-3a23b8df1f01",
 *       "policy": "...",
 *       "x-oss-signature": "..."
 *     }
 *   },
 *   "expiresAt": "2026-08-07T16:05:00+08:00"
 * }
 * }</pre>
 */
public record PrepareProductImageUploadResponse(
        String fileId,
        String status,
        UploadInstruction upload,
        OffsetDateTime expiresAt
) {
    public record UploadInstruction(
            String url,
            UploadFields fields
    ) {
    }

    public record UploadFields(
            String key,
            @JsonProperty("Content-Type") String contentType,
            @JsonProperty("success_action_status") String successActionStatus,
            @JsonProperty("x-oss-forbid-overwrite") String forbidOverwrite,
            String policy,
            @JsonProperty("x-oss-signature-version") String signatureVersion,
            @JsonProperty("x-oss-credential") String credential,
            @JsonProperty("x-oss-date") String date,
            @JsonProperty("x-oss-signature") String signature
    ) {
    }
}
