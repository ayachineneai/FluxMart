package org.ayachinene.app.product.creation;

import org.ayachinene.api.product.data.CreateProductRequest;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.shared.validate.AmountValidators;
import org.ayachinene.shared.validate.ListValidators;
import org.ayachinene.shared.validate.Validators;

import java.math.BigDecimal;

import static org.ayachinene.shared.validate.ListValidators.*;
import static org.ayachinene.shared.validate.StringValidators.text;
import static org.ayachinene.shared.validate.Validators.notNull;
import static org.ayachinene.shared.validate.Validators.whenPresent;

public final class ProductValidator {

    private ProductValidator() {
    }

    public static CreateProductRequest validate(CreateProductRequest request) {
        notNull(request, "request");

        return new CreateProductRequest(
            text(50).v(request.title(), "title"),
            whenPresent(text(50)).v(request.subtitle(), "subtitle"),
            text(5000).v(request.description(), "description"),
            text(64).v(request.categoryCode(), "categoryCode"),
            notNull(request.primaryImageFileId(), "primaryImageFileId"),
            ListValidators.<UUID7>nullAsEmpty()
                .c(each(Validators::notNull))
                .c(unique())
                .v(request.galleryImageFileIds(), "galleryImageFileIds"),
            ListValidators.<CreateProductRequest.SpecificationRequest>nullAsEmpty()
                .c(each((specification, field) -> {
                    notNull(specification, field);
                    return new CreateProductRequest.SpecificationRequest(
                        text(50).v(specification.name(), field + ".name"),
                        each(text(50))
                            .c(notEmpty())
                            .c(unique())
                            .v(specification.values(), field + ".values")
                    );
                }))
                .c(unique(CreateProductRequest.SpecificationRequest::name))
                .v(request.specifications(), "specifications"),
            ListValidators.<CreateProductRequest.SkuRequest>nullAsEmpty()
                .c(each((sku, field) -> {
                    notNull(sku, field);
                    return new CreateProductRequest.SkuRequest(
                        whenPresent(text(64)).v(
                            sku.merchantSkuCode(),
                            field + ".merchantSkuCode"
                        ),
                        AmountValidators.positive()
                            .c(AmountValidators.range(
                                BigDecimal.ZERO,
                                new BigDecimal("99999999.99")
                            ))
                            .c(AmountValidators.maxFractionDigits(2))
                            .v(
                                notNull(sku.price(), field + ".price"),
                                field + ".price"
                            ),
                        sku.imageFileId(),
                        ListValidators.<CreateProductRequest.SelectionRequest>nullAsEmpty()
                            .v(sku.selections(), field + ".selections")
                    );
                }))
                .c(notEmpty())
                .v(request.skus(), "skus")
        );
    }

}
