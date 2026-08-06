package org.ayachinene.app.domain.product;

import org.ayachinene.app.domain.file.FileResourceId;
import org.ayachinene.app.domain.product.creation.CreateProductInput;
import org.ayachinene.utils.Lists;
import org.ayachinene.utils.Validates;

import java.util.List;
import java.util.Objects;

public final class ProductValidator {

    private static final int MAX_TITLE_LENGTH = 50;
    private static final int MAX_SUBTITLE_LENGTH = 50;

    private ProductValidator() {
    }

    public static CreateProductInput validate(CreateProductInput input) {
        Objects.requireNonNull(input, "input must not be null");
        return new CreateProductInput(
                title(input.title()),
                subtitle(input.subtitle()),
                description(input.description()),
                categoryCode(input.categoryCode()),
                fileResourceId(input.primaryImageFileId(), "primaryImageFileId"),
                galleryImageFileIds(input.galleryImageFileIds()),
                input.specifications(),
                input.skus()
        );
    }

    public static String title(String value) {
        return Validates.requiredText(value, "title", MAX_TITLE_LENGTH);
    }

    public static String subtitle(String value) {
        if (value == null) {
            return null;
        }
        return Validates.requiredText(value, "subtitle", MAX_SUBTITLE_LENGTH);
    }

    public static String description(String value) {
        return Validates.requiredText(value, "description");
    }

    public static CategoryCode categoryCode(CategoryCode value) {
        return Validates.requireNonNull(value, "categoryCode");
    }

    public static FileResourceId fileResourceId(FileResourceId value, String field) {
        return Validates.requireNonNull(value, field);
    }

    public static List<FileResourceId> galleryImageFileIds(List<FileResourceId> values) {
        var normalized = Lists.nullToEmpty(values).stream()
                .map(value -> fileResourceId(value, "galleryImageFileIds element"))
                .toList();
        Validates.requireUnique(
                normalized,
                "galleryImageFileIds must not contain duplicates"
        );
        return normalized;
    }

}
