package org.ayachinene.app.file.productimage;

import org.ayachinene.utils.Strings;
import org.ayachinene.utils.Validates;

import java.util.Locale;
import java.util.Set;

public final class ProductImageValidator {

    private static final int MAX_FILENAME_LENGTH = 255;
    private static final long MAX_SIZE_IN_BYTES = 10L * 1024 * 1024;
    private static final Set<String> CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private ProductImageValidator() {
    }

    public static PrepareProductImageUploadInput validate(
            PrepareProductImageUploadInput input
    ) {
        return new PrepareProductImageUploadInput(
                filename(input.filename()),
                contentType(input.contentType()),
                size(input.sizeInBytes())
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

    private static String contentType(String value) {
        var contentType = Validates.requiredText(value, "contentType")
                .toLowerCase(Locale.ROOT);
        Validates.require(
                CONTENT_TYPES.contains(contentType),
                "contentType must be image/jpeg, image/png, or image/webp"
        );
        return contentType;
    }

    private static long size(Long value) {
        long size = Validates.requireNonNull(value, "sizeInBytes");
        Validates.require(size > 0, "sizeInBytes must be greater than zero");
        Validates.require(
                size <= MAX_SIZE_IN_BYTES,
                "sizeInBytes must not exceed 10 MiB"
        );
        return size;
    }
}
