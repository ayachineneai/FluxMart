package org.ayachinene.app.product;

import org.ayachinene.app.exception.ValidationException;
import org.ayachinene.app.product.creation.CreateProductInput;
import org.ayachinene.app.product.creation.CreateProductValidator;
import org.ayachinene.app.product.domain.CategoryCode;
import org.ayachinene.shared.uuid7.UUID7s;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateProductValidatorTest {

    @Test
    void normalizesAValidInput() {
        var result = CreateProductValidator.validate(input(
                List.of(new CreateProductInput.Specification(
                        " 颜色 ",
                        List.of(" 黑色 ")
                )),
                List.of(sku(new CreateProductInput.Selection(
                        " 颜色 ",
                        " 黑色 "
                )))
        ));

        assertEquals("T-Shirt", result.title());
        assertEquals("颜色", result.specifications().getFirst().name());
        assertEquals("黑色", result.specifications().getFirst().values().getFirst());
        assertEquals(new BigDecimal("99.00"), result.skus().getFirst().price());
        assertEquals(
                "颜色",
                result.skus().getFirst().selections().getFirst().specification()
        );
    }

    @Test
    void rejectsInvalidSpecificationAndSkuConfiguration() {
        assertThrows(ValidationException.class, () -> CreateProductValidator.validate(
                input(
                        List.of(new CreateProductInput.Specification(
                                "颜色",
                                List.of("黑色")
                        )),
                        List.of(sku(new CreateProductInput.Selection(
                                "尺码",
                                "M"
                        )))
                )
        ));
    }

    @Test
    void rejectsDuplicateGalleryImages() {
        var fileId = UUID7s.generate();
        var input = new CreateProductInput(
                "T-Shirt",
                null,
                "Cotton",
                new CategoryCode("TSHIRT"),
                UUID7s.generate(),
                List.of(fileId, fileId),
                List.of(),
                List.of(sku())
        );

        assertThrows(
                ValidationException.class,
                () -> CreateProductValidator.validate(input)
        );
    }

    @Test
    void rejectsInvalidProductFields() {
        var input = new CreateProductInput(
                " ",
                null,
                "Cotton",
                new CategoryCode("TSHIRT"),
                UUID7s.generate(),
                List.of(),
                List.of(),
                List.of(sku())
        );

        assertThrows(
                ValidationException.class,
                () -> CreateProductValidator.validate(input)
        );
    }

    @Test
    void rejectsDuplicatedSpecificationsAndValues() {
        assertThrows(ValidationException.class, () -> CreateProductValidator.validate(
                input(
                        List.of(
                                new CreateProductInput.Specification(
                                        "颜色",
                                        List.of("黑色")
                                ),
                                new CreateProductInput.Specification(
                                        " 颜色 ",
                                        List.of("白色")
                                )
                        ),
                        List.of(sku(
                                new CreateProductInput.Selection(
                                        "颜色",
                                        "黑色"
                                ),
                                new CreateProductInput.Selection(
                                        " 颜色 ",
                                        "白色"
                                )
                        ))
                )
        ));
        assertThrows(ValidationException.class, () -> CreateProductValidator.validate(
                input(
                        List.of(new CreateProductInput.Specification(
                                "颜色",
                                List.of("黑色", " 黑色 ")
                        )),
                        List.of(sku(new CreateProductInput.Selection(
                                "颜色",
                                "黑色"
                        )))
                )
        ));
    }

    @Test
    void rejectsInvalidSkuPriceAndDuplicateSelections() {
        assertThrows(ValidationException.class, () -> CreateProductValidator.validate(
                input(
                        List.of(),
                        List.of(new CreateProductInput.Sku(
                                null,
                                BigDecimal.ZERO,
                                null,
                                List.of()
                        ))
                )
        ));
        assertThrows(ValidationException.class, () -> CreateProductValidator.validate(
                input(
                        List.of(new CreateProductInput.Specification(
                                "颜色",
                                List.of("黑色", "白色")
                        )),
                        List.of(sku(
                                new CreateProductInput.Selection(
                                        "颜色",
                                        "黑色"
                                ),
                                new CreateProductInput.Selection(
                                        "颜色",
                                        "白色"
                                )
                        ))
                )
        ));
    }

    @Test
    void rejectsDuplicatedSkuConfigurations() {
        var specifications = List.of(
                new CreateProductInput.Specification(
                        "颜色",
                        List.of("黑色")
                ),
                new CreateProductInput.Specification(
                        "尺码",
                        List.of("M")
                )
        );

        assertThrows(ValidationException.class, () -> CreateProductValidator.validate(
                input(
                        specifications,
                        List.of(
                                sku(
                                        new CreateProductInput.Selection(
                                                "颜色",
                                                "黑色"
                                        ),
                                        new CreateProductInput.Selection(
                                                "尺码",
                                                "M"
                                        )
                                ),
                                new CreateProductInput.Sku(
                                        "SKU-002",
                                        BigDecimal.TEN,
                                        null,
                                        List.of(
                                                new CreateProductInput.Selection(
                                                        "尺码",
                                                        "M"
                                                ),
                                                new CreateProductInput.Selection(
                                                        "颜色",
                                                        "黑色"
                                                )
                                        )
                                )
                        )
                )
        ));
    }

    @Test
    void acceptsOneDefaultSkuWithoutSpecifications() {
        assertDoesNotThrow(() -> CreateProductValidator.validate(
                input(List.of(), List.of(sku()))
        ));
    }

    private static CreateProductInput input(
            List<CreateProductInput.Specification> specifications,
            List<CreateProductInput.Sku> skus
    ) {
        return new CreateProductInput(
                " T-Shirt ",
                null,
                " Cotton ",
                new CategoryCode("TSHIRT"),
                UUID7s.generate(),
                List.of(),
                specifications,
                skus
        );
    }

    private static CreateProductInput.Sku sku(
            CreateProductInput.Selection... selections
    ) {
        return new CreateProductInput.Sku(
                " SKU-001 ",
                new BigDecimal("99"),
                null,
                List.of(selections)
        );
    }
}
