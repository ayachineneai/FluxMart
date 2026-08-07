package org.ayachinene.app.domain.product;

import org.ayachinene.app.domain.product.creation.CreateProductInput;
import org.ayachinene.app.domain.product.creation.ProductCreation;
import org.ayachinene.app.domain.product.sku.SkuValidator;
import org.ayachinene.app.domain.product.sku.Skus;
import org.ayachinene.app.domain.product.specification.SpecificationValidator;
import org.ayachinene.app.domain.product.specification.Specifications;

import java.util.Objects;

public final class Products {

    private Products() {
    }

    public static ProductCreation create(
            ProductCode productCode,
            CreateProductInput input
    ) {
        Objects.requireNonNull(productCode, "productCode must not be null");
        Objects.requireNonNull(input, "input must not be null");

        var productInput = ProductValidator.validate(input);
        var specificationInputs = SpecificationValidator.validate(input.specifications());
        var skuInputs = SkuValidator.validate(input.skus());
        SkuSpecificationValidator.validate(specificationInputs, skuInputs);

        var product = createProduct(productCode, productInput);
        var specifications = Specifications.create(specificationInputs);
        var skus = Skus.create(skuInputs, specifications);
        return new ProductCreation(product, specifications, skus);
    }

    private static Product createProduct(
            ProductCode productCode,
            CreateProductInput input
    ) {
        return new Product(
                productCode,
                ProductStatus.DRAFT,
                input.title(),
                input.subtitle(),
                input.description(),
                input.categoryCode(),
                input.primaryImageFileId(),
                input.galleryImageFileIds()
        );
    }

}
