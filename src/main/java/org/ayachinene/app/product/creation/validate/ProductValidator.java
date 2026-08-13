package org.ayachinene.app.product.creation.validate;

import org.ayachinene.api.product.data.CreateProductRequest;

import static org.ayachinene.utils.Validates.notNull;
import static org.ayachinene.utils.Validates.optionalText;
import static org.ayachinene.utils.Validates.text;

public final class ProductValidator {

    private ProductValidator() {
    }

    public static CreateProductRequest validate(CreateProductRequest request) {
        notNull(request, "request");

        text(request.title(), "title", 50);
        optionalText(request.subtitle(), "subtitle", 50);
        text(request.description(), "description", 5000);
        text(request.categoryCode(), "categoryCode", 64);
        notNull(request.primaryImageFileId(), "primaryImageFileId");

        SpecificationValidator.validate(request.specifications());
        SkuValidator.validate(request.skus());
        SkuSpecificationValidator.validate(
            request.specifications(),
            request.skus()
        );
        return request;
    }
}
