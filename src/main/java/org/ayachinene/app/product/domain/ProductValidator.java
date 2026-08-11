package org.ayachinene.app.product.domain;

import org.ayachinene.app.product.creation.ProductDetailsInput;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.utils.Lists;
import org.ayachinene.utils.Streams;
import org.ayachinene.utils.Validates;
import org.ayachinene.utils.Values;

import java.util.List;

public final class ProductValidator {

    private static final int MAX_TITLE_LENGTH = 50;
    private static final int MAX_SUBTITLE_LENGTH = 50;

    private ProductValidator() {
    }

    public static ProductDetailsInput validate(ProductDetailsInput input) {
        return new ProductDetailsInput(
                title(input.title()),
                subtitle(input.subtitle()),
                description(input.description()),
                categoryCode(input.categoryCode()),
                fileResourceId(input.primaryImageFileId(), "primaryImageFileId"),
                galleryImageFileIds(input.galleryImageFileIds())
        );
    }

    public static String title(String value) {
        return Validates.requiredText(value, "title", MAX_TITLE_LENGTH);
    }

    public static String subtitle(String value) {
        return Values.map(value,
            subtitle -> Validates.requiredText(subtitle, "subtitle", MAX_SUBTITLE_LENGTH)
        );
    }

    public static String description(String value) {
        return Validates.requiredText(value, "description");
    }

    public static CategoryCode categoryCode(CategoryCode value) {
        return Validates.requireNonNull(value, "categoryCode");
    }

    public static UUID7 fileResourceId(UUID7 value, String field) {
        return Validates.requireNonNull(value, field);
    }

    public static List<UUID7> galleryImageFileIds(List<UUID7> values) {
        var normalized = Streams.of(values)
            .map(value -> fileResourceId(value, "galleryImageFileIds element"))
            .toList();
        Validates.require(Lists.isUnique(normalized),
            "galleryImageFileIds must not contain duplicates"
        );
        return normalized;
    }

}
