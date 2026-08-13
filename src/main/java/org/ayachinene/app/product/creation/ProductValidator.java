package org.ayachinene.app.product.creation;

import org.ayachinene.api.product.data.CreateProductRequest;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.shared.validate.AmountValidators;
import org.ayachinene.shared.validate.ListValidators;
import org.ayachinene.shared.validate.Validators;

import java.math.BigDecimal;
import java.util.List;

import static org.ayachinene.shared.validate.ListValidators.each;
import static org.ayachinene.shared.validate.ListValidators.unique;
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
            galleryImageFileIds(request.galleryImageFileIds()),
            specifications(request.specifications()),
            skus(request.skus())
        );
    }

    private static List<UUID7> galleryImageFileIds(List<UUID7> values) {
        return ListValidators.<UUID7>nullAsEmpty()
            .c(unique())
            .c(each(Validators::notNull))
            .v(values, "galleryImageFileIds");
    }

    private static List<CreateProductRequest.SpecificationRequest> specifications(
        List<CreateProductRequest.SpecificationRequest> values
    ) {
        return ListValidators.<CreateProductRequest.SpecificationRequest>nullAsEmpty()
            .c(each(ProductValidator::specification))
            .c(unique(CreateProductRequest.SpecificationRequest::name))
            .v(values, "specifications");
    }

    private static CreateProductRequest.SpecificationRequest specification(
        CreateProductRequest.SpecificationRequest specification,
        String field
    ) {
        notNull(specification, field);
        return new CreateProductRequest.SpecificationRequest(
            text(50).v(specification.name(), field + ".name"),
            ListValidators.<String>notEmpty()
                .c(each(text(50)))
                .c(unique())
                .v(specification.values(), field + ".values")
        );
    }

    private static List<CreateProductRequest.SkuRequest> skus(
        List<CreateProductRequest.SkuRequest> values
    ) {
        return ListValidators.<CreateProductRequest.SkuRequest>notEmpty()
            .c(each(ProductValidator::sku))
            .v(values, "skus");
    }

    private static CreateProductRequest.SkuRequest sku(
        CreateProductRequest.SkuRequest sku,
        String field
    ) {
        notNull(sku, field);
        return new CreateProductRequest.SkuRequest(
            whenPresent(text(64)).v(sku.merchantSkuCode(), field + ".merchantSkuCode"),
            AmountValidators.positive()
                .c(AmountValidators.range(BigDecimal.ZERO, new BigDecimal("99999999.99")))
                .c(AmountValidators.maxFractionDigits(2))
                .v(notNull(sku.price(), field + ".price"), field + ".price"),
            sku.imageFileId(),
            ListValidators.<CreateProductRequest.SelectionRequest>nullAsEmpty()
                .c(each(ProductValidator::selection))
                .v(sku.selections(), field + ".selections")
        );
    }

    private static CreateProductRequest.SelectionRequest selection(
        CreateProductRequest.SelectionRequest selection,
        String field
    ) {
        notNull(selection, field);
        return new CreateProductRequest.SelectionRequest(
            text(50).v(selection.specification(), field + ".specification"),
            text(50).v(selection.value(), field + ".value")
        );
    }
}
