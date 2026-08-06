package org.ayachinene.api.product;

import org.ayachinene.app.domain.file.FileResourceId;
import org.ayachinene.app.domain.product.CategoryCode;
import org.ayachinene.app.domain.product.creation.CreateProductInput;
import org.ayachinene.app.domain.product.creation.SelectionInput;
import org.ayachinene.app.domain.product.creation.SkuInput;
import org.ayachinene.app.domain.product.creation.SpecificationInput;
import org.ayachinene.app.exception.ValidationException;
import org.ayachinene.app.uuid7.UUID7s;
import org.ayachinene.utils.Lists;
import org.ayachinene.utils.Validates;

import java.math.BigDecimal;
import java.util.List;

public record CreateProductRequest(
        String title,
        String subtitle,
        String description,
        String categoryCode,
        String primaryImageFileId,
        List<String> galleryImageFileIds,
        List<SpecificationRequest> specifications,
        List<SkuRequest> skus
) {

    public CreateProductInput toInput() {
        return new CreateProductInput(
                title,
                subtitle,
                description,
                categoryCode(categoryCode),
                fileResourceId(primaryImageFileId, "primaryImageFileId"),
                Lists.nullToEmpty(galleryImageFileIds).stream()
                        .map(value -> fileResourceId(value, "galleryImageFileIds element"))
                        .toList(),
                Lists.nullToEmpty(specifications).stream()
                        .map(SpecificationRequest::toInput)
                        .toList(),
                Lists.nullToEmpty(skus).stream()
                        .map(SkuRequest::toInput)
                        .toList()
        );
    }

    private static CategoryCode categoryCode(String value) {
        return new CategoryCode(Validates.requiredText(value, "categoryCode"));
    }

    private static FileResourceId fileResourceId(String value, String field) {
        var normalized = Validates.requiredText(value, field);
        var parsed = UUID7s.fromString(normalized);
        if (parsed.isLeft()) {
            throw new ValidationException(field + " must be a UUIDv7");
        }
        return new FileResourceId(parsed.get());
    }

    private static FileResourceId optionalFileResourceId(String value, String field) {
        return value == null ? null : fileResourceId(value, field);
    }

    public record SpecificationRequest(
            String name,
            List<String> values
    ) {

        private SpecificationInput toInput() {
            return new SpecificationInput(name, values);
        }
    }

    public record SkuRequest(
            String merchantSkuCode,
            BigDecimal price,
            String imageFileId,
            List<SelectionRequest> selections
    ) {

        private SkuInput toInput() {
            return new SkuInput(
                    merchantSkuCode,
                    price,
                    optionalFileResourceId(imageFileId, "sku imageFileId"),
                    Lists.nullToEmpty(selections).stream()
                            .map(SelectionRequest::toInput)
                            .toList()
            );
        }
    }

    public record SelectionRequest(
            String specification,
            String value
    ) {

        private SelectionInput toInput() {
            return new SelectionInput(specification, value);
        }
    }
}
