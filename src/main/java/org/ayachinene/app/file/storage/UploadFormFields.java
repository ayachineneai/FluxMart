package org.ayachinene.app.file.storage;

public record UploadFormFields(
        String key,
        String contentType,
        String successActionStatus,
        String forbidOverwrite,
        String policy,
        String signatureVersion,
        String credential,
        String date,
        String signature
) {
}
