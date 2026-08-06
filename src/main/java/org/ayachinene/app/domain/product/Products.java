package org.ayachinene.app.domain.product;

import org.ayachinene.app.domain.product.creation.CreateProductInput;
import org.ayachinene.app.domain.product.creation.ProductCreation;
import org.ayachinene.app.domain.product.sku.Skus;
import org.ayachinene.app.domain.product.specification.Specifications;

import java.util.Objects;

public final class Products {

    private Products() {
    }

    public static ProductCreation mkProductCreation(
            ProductCode productCode,
            CreateProductInput input
    ) {
        var product = mkProduct(productCode, input);
        var specifications = Specifications.mkSpecifications(input.specifications());
        var skus = Skus.mkSkus(input.skus(), specifications);
        return new ProductCreation(product, specifications, skus);
    }

    public static Product mkProduct(
            ProductCode productCode,
            CreateProductInput input
    ) {
        Objects.requireNonNull(productCode, "productCode must not be null");
        Objects.requireNonNull(input, "input must not be null");

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
