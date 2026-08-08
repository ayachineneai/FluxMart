package org.ayachinene.app.file.domain;

import org.ayachinene.app.file.upload.PrepareProductImageUploadInput;
import org.ayachinene.utils.Strings;
import org.ayachinene.utils.Validates;

import java.util.Locale;
import java.util.Set;

public final class FileValidator {

    private static final int MAX_FILENAME_LENGTH = 255;
    private static final long MAX_PRODUCT_IMAGE_SIZE_IN_BYTES =
            10L * 1024 * 1024;
    private static final Set<String> PRODUCT_IMAGE_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private FileValidator() {
    }

    public static PrepareProductImageUploadInput productImage(
            PrepareProductImageUploadInput input
    ) {
        return new PrepareProductImageUploadInput(
                filename(input.filename()),
                productImageContentType(input.contentType()),
                productImageSize(input.sizeInBytes())
        );
    }

    private static String filename(String value) {
        var filename = Validates.requiredText(
                value,
                "filename",
                MAX_FILENAME_LENGTH
        );
        Validates.require(
                Strings.notContains(filename, '/', '\\'),
                "filename must not contain path separators"
        );
        Validates.require(
                filename.chars().noneMatch(Character::isISOControl),
                "filename must not contain control characters"
        );
        return filename;
    }

    private static String productImageContentType(String value) {
        var contentType = Validates.requiredText(value, "contentType")
                .toLowerCase(Locale.ROOT);
        Validates.require(
                PRODUCT_IMAGE_CONTENT_TYPES.contains(contentType),
                "contentType must be image/jpeg, image/png, or image/webp"
        );
        return contentType;
    }

    private static long productImageSize(Long value) {
        long size = Validates.requireNonNull(value, "sizeInBytes");
        Validates.require(size > 0, "sizeInBytes must be greater than zero");
        Validates.require(
                size <= MAX_PRODUCT_IMAGE_SIZE_IN_BYTES,
                "sizeInBytes must not exceed 10 MiB"
        );
        return size;
    }
}
