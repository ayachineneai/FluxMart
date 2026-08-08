package org.ayachinene.api.file.data;

/**
 * <pre>{@code
 * {
 *   "filename": "black-shirt.png",
 *   "contentType": "image/png",
 *   "sizeInBytes": 183421
 * }
 * }</pre>
 */
public record PrepareProductImageUploadRequest(
        String filename,
        String contentType,
        Long sizeInBytes
) {
}
