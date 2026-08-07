package org.ayachinene.app.domain.product;

import org.ayachinene.app.domain.product.creation.CreateProductInput;
import org.ayachinene.app.domain.product.creation.ProductInput;
import org.ayachinene.app.domain.product.creation.ProductCreation;
import org.ayachinene.app.domain.product.sku.SkuValidator;
import org.ayachinene.app.domain.product.sku.Skus;
import org.ayachinene.app.domain.product.specification.SpecificationValidator;
import org.ayachinene.app.domain.product.specification.Specifications;

public final class Products {

    private Products() {
    }

    public static ProductCreation create(CreateProductInput input) {
        var productInput = ProductValidator.validate(input.product());
        var specificationInputs = SpecificationValidator.validate(input.specifications());
        var skuInputs = SkuValidator.validate(input.skus());
        SkuSpecificationValidator.validate(specificationInputs, skuInputs);

        var product = createProduct(productInput);
        var specifications = Specifications.create(specificationInputs);
        var skus = Skus.create(skuInputs, specifications);
        return new ProductCreation(product, specifications, skus);
    }

    private static Product createProduct(ProductInput input) {
        return new Product(
                ProductCode.generate(),
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
