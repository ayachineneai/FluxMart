package org.ayachinene.app.product.sku;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.product.domain.sku.Sku;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.product.domain.sku.SkuStatus;
import org.ayachinene.app.product.domain.sku.SpecificationSelection;
import org.ayachinene.app.product.domain.specification.SpecificationCode;
import org.ayachinene.app.product.domain.specification.SpecificationValueCode;
import org.ayachinene.app.exception.ValidationException;
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
                SkuCode.generate(),
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
        var specificationCode = SpecificationCode.generate();

        assertThrows(ValidationException.class, () -> new Sku(
                SkuCode.generate(),
                null,
                SkuStatus.ENABLED,
                new Money(new BigDecimal("99.00")),
                null,
                List.of(
                        new SpecificationSelection(
                                specificationCode,
                                SpecificationValueCode.generate()
                        ),
                        new SpecificationSelection(
                                specificationCode,
                                SpecificationValueCode.generate()
                        )
                )
        ));
    }

    private static SpecificationSelection selection() {
        return new SpecificationSelection(
                SpecificationCode.generate(),
                SpecificationValueCode.generate()
        );
    }
}
