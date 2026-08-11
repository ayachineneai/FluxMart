package org.ayachinene.app.product.domain;

import org.ayachinene.app.product.creation.CreateProductInput;
import org.ayachinene.app.product.creation.ProductDetailsInput;
import org.ayachinene.app.product.creation.ProductCreation;
import org.ayachinene.app.product.publication.ProductPublication;
import org.ayachinene.app.product.publication.ProductPublicationState;
import org.ayachinene.app.product.domain.sku.SkuValidator;
import org.ayachinene.app.product.domain.sku.Skus;
import org.ayachinene.app.product.domain.specification.SpecificationValidator;
import org.ayachinene.app.product.domain.specification.Specifications;
import org.ayachinene.utils.Values;

import java.util.Set;

public final class Products {

    private static final Set<ProductStatus> PUBLISHABLE_STATUSES = Set.of(
        ProductStatus.DRAFT,
        ProductStatus.OFF_SALE
    );

    private Products() {
    }

    public static ProductCreation toCreation(CreateProductInput input) {
        var details = ProductValidator.validate(input.details());
        var specificationInputs = SpecificationValidator.validate(input.specifications());
        var skuInputs = SkuValidator.validate(input.skus());
        SkuSpecificationValidator.validate(specificationInputs, skuInputs);

        var product = createProduct(details);
        var specifications = Specifications.create(specificationInputs);
        var skus = Skus.create(skuInputs, specifications);
        return new ProductCreation(product, specifications, skus);
    }

    private static Product createProduct(ProductDetailsInput input) {
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

    public static ProductPublication publish(
        ProductPublicationState product,
        long expectedVersion,
        boolean hasSku
    ) {
        if (product.version() != expectedVersion) {
            throw new ProductVersionConflictException(
                product.productCode(),
                expectedVersion
            );
        }
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

}
