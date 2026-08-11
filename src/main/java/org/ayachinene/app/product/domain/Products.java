package org.ayachinene.app.product.domain;

import org.ayachinene.app.product.publication.ProductPublication;
import org.ayachinene.app.product.publication.ProductPublicationState;
import org.ayachinene.utils.Values;

import java.util.Set;

public final class Products {

    private static final Set<ProductStatus> PUBLISHABLE_STATUSES = Set.of(
        ProductStatus.DRAFT,
        ProductStatus.OFF_SALE
    );

    private Products() {
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
