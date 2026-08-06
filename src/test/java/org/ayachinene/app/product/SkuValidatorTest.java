package org.ayachinene.app.product;

import org.ayachinene.app.domain.product.sku.SkuValidator;
import org.ayachinene.app.domain.product.creation.SelectionInput;
import org.ayachinene.app.domain.product.creation.SkuInput;
import org.ayachinene.app.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SkuValidatorTest {

    @Test
    void normalizesSkuInput() {
        var result = SkuValidator.validate(List.of(new SkuInput(
                " SKU-001 ",
                BigDecimal.ONE,
                null,
                List.of(new SelectionInput(" 颜色 ", " 黑色 "))
        )));

        assertEquals("SKU-001", result.getFirst().merchantSkuCode());
        assertEquals(new BigDecimal("1.00"), result.getFirst().price());
        assertEquals(
                List.of(new SelectionInput("颜色", "黑色")),
                result.getFirst().selections()
        );
    }

    @Test
    void rejectsInvalidSkuInput() {
        assertThrows(ValidationException.class, () -> SkuValidator.validate(List.of(
                new SkuInput(null, BigDecimal.ZERO, null, List.of())
        )));
        assertThrows(ValidationException.class, () -> SkuValidator.validate(List.of(
                new SkuInput(
                        null,
                        BigDecimal.ONE,
                        null,
                        List.of(
                                new SelectionInput("颜色", "黑色"),
                                new SelectionInput("颜色", "白色")
                        )
                )
        )));
        assertThrows(ValidationException.class, () -> SkuValidator.validate(List.of(
                new SkuInput(null, new BigDecimal("100000000.00"), null, List.of())
        )));
    }

    @Test
    void rejectsDuplicatedConfigurations() {
        assertThrows(ValidationException.class, () -> SkuValidator.validate(List.of(
                new SkuInput(
                        "SKU-1",
                        BigDecimal.ONE,
                        null,
                        List.of(
                                new SelectionInput("颜色", "黑色"),
                                new SelectionInput("尺码", "M")
                        )
                ),
                new SkuInput(
                        "SKU-2",
                        BigDecimal.TEN,
                        null,
                        List.of(
                                new SelectionInput("尺码", "M"),
                                new SelectionInput("颜色", "黑色")
                        )
                )
        )));
    }

    @Test
    void treatsMerchantSkuCodesAsCaseSensitive() {
        var result = SkuValidator.validate(List.of(
                new SkuInput(
                        "SKU-A",
                        BigDecimal.ONE,
                        null,
                        List.of(new SelectionInput("颜色", "黑色"))
                ),
                new SkuInput(
                        "sku-a",
                        BigDecimal.ONE,
                        null,
                        List.of(new SelectionInput("颜色", "白色"))
                )
        ));

        assertEquals(2, result.size());
    }
}
