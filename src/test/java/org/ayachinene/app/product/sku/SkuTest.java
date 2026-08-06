package org.ayachinene.app.product.sku;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.domain.product.sku.Sku;
import org.ayachinene.app.domain.product.sku.SkuCode;
import org.ayachinene.app.domain.product.sku.SkuStatus;
import org.ayachinene.app.domain.product.sku.SpecificationSelection;
import org.ayachinene.app.domain.product.specification.SpecificationId;
import org.ayachinene.app.domain.product.specification.SpecificationValueId;
import org.ayachinene.app.uuid7.UUID7s;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SkuTest {

    @Test
    void copiesSpecificationSelections() {
        var selections = new java.util.ArrayList<SpecificationSelection>();
        var sku = new Sku(
                new SkuCode(UUID7s.generate()),
                "MERCHANT-001",
                SkuStatus.ENABLED,
                new Money(new BigDecimal("99.00")),
                null,
                selections
        );

        selections.add(selection());

        assertEquals(List.of(), sku.specificationSelections());
    }

    @Test
    void rejectsTwoValuesForTheSameSpecification() {
        var specificationId = new SpecificationId(UUID7s.generate());

        assertThrows(IllegalArgumentException.class, () -> new Sku(
                new SkuCode(UUID7s.generate()),
                null,
                SkuStatus.ENABLED,
                new Money(new BigDecimal("99.00")),
                null,
                List.of(
                        new SpecificationSelection(
                                specificationId,
                                new SpecificationValueId(UUID7s.generate())
                        ),
                        new SpecificationSelection(
                                specificationId,
                                new SpecificationValueId(UUID7s.generate())
                        )
                )
        ));
    }

    private static SpecificationSelection selection() {
        return new SpecificationSelection(
                new SpecificationId(UUID7s.generate()),
                new SpecificationValueId(UUID7s.generate())
        );
    }
}
