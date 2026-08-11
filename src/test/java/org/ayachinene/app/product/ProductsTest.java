package org.ayachinene.app.product;

import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.app.product.domain.ProductVersionConflictException;
import org.ayachinene.app.product.domain.Products;
import org.ayachinene.app.product.publication.ProductPublicationState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductsTest {

    @Test
    void rejectsPublishingWithUnexpectedVersion() {
        var productCode = ProductCode.generate();
        var state = new ProductPublicationState(
                productCode,
                ProductStatus.DRAFT,
                4L
        );

        assertThrows(
                ProductVersionConflictException.class,
                () -> Products.publish(state, 3L, true)
        );
    }
}
