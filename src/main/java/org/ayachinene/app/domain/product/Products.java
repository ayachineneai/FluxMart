package org.ayachinene.app.domain.product;

import org.ayachinene.app.domain.product.creation.CreateProductInput;
import org.ayachinene.app.domain.product.creation.ProductInput;
import org.ayachinene.app.domain.product.creation.ProductCreation;
import org.ayachinene.app.domain.product.publication.ProductPublication;
import org.ayachinene.app.domain.product.publication.ProductPublicationState;
import org.ayachinene.app.domain.product.sku.SkuValidator;
import org.ayachinene.app.domain.product.sku.Skus;
import org.ayachinene.app.domain.product.specification.SpecificationValidator;
import org.ayachinene.app.domain.product.specification.Specifications;
import org.ayachinene.utils.Values;

import java.util.Set;

public final class Products {

    private static final Set<ProductStatus> PUBLISHABLE_STATUSES = Set.of(
        ProductStatus.DRAFT,
        ProductStatus.OFF_SALE
    );

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

    public static ProductPublication publish(
        ProductPublicationState product,
        boolean hasSku
    ) {
        if (Values.notIn(PUBLISHABLE_STATUSES, product.status())) {
            throw new ProductCannotBePublishedException(
                product.productCode(),
                "status must be DRAFT or OFF_SALE"
            );
        }
        if (!hasSku) {
            throw new ProductCannotBePublishedException(
                product.productCode(),
                "product must contain at least one SKU"
            );
        }
        return new ProductPublication(
            product.productCode(),
            ProductStatus.ON_SALE
        );
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
