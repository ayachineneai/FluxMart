package org.ayachinene.app.product;

import org.ayachinene.app.domain.product.CategoryCode;
import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.domain.product.Products;
import org.ayachinene.app.domain.product.creation.CreateProductInput;
import org.ayachinene.app.domain.product.creation.SelectionInput;
import org.ayachinene.app.domain.product.creation.SkuInput;
import org.ayachinene.app.domain.product.creation.SpecificationInput;
import org.ayachinene.app.domain.product.sku.SkuStatus;
import org.ayachinene.app.domain.product.specification.SpecificationStatus;
import org.ayachinene.shared.uuid7.UUID7s;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductCreationTest {

    @Test
    void createsSpecificationsAndResolvesSkuSelections() {
        var creation = Products.create(
                ProductCode.generate(),
                new CreateProductInput(
                        "T-Shirt",
                        null,
                        "Cotton",
                        new CategoryCode("TSHIRT"),
                        UUID7s.generate(),
                        List.of(),
                        List.of(new SpecificationInput("颜色", List.of("黑色"))),
                        List.of(new SkuInput(
                                "TSHIRT-BLACK",
                                new BigDecimal("99.00"),
                                null,
                                List.of(new SelectionInput("颜色", "黑色"))
                        ))
                )
        );

        var specification = creation.specifications().getFirst();
        var value = specification.values().getFirst();
        var sku = creation.skus().getFirst();
        var selection = sku.specificationSelections().getFirst();

        assertEquals(SpecificationStatus.ENABLED, specification.status());
        assertEquals(SkuStatus.DISABLED, sku.status());
        assertEquals(specification.specificationId(), selection.specificationId());
        assertEquals(value.specificationValueId(), selection.specificationValueId());
    }
}
