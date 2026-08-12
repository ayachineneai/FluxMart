package org.ayachinene.app.product.creation;

import org.ayachinene.api.product.data.CreateProductRequest;
import org.ayachinene.shared.uuid7.UUID7s;
import org.ayachinene.shared.validate.ValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductValidatorTest {

    @Test
    void normalizesProductAndSpecificationText() {
        var request = request(List.of(
            new CreateProductRequest.SpecificationRequest(
                " Color ",
                List.of(" Red ", "Blue")
            )
        ));

        var validated = ProductValidator.validate(request);

        assertEquals("Product", validated.title());
        assertNull(validated.subtitle());
        assertEquals("Description", validated.description());
        assertEquals("category", validated.categoryCode());
        assertEquals("Color", validated.specifications().getFirst().name());
        assertEquals(
            List.of("Red", "Blue"),
            validated.specifications().getFirst().values()
        );
        assertEquals("SKU-1", validated.skus().getFirst().merchantSkuCode());
    }

    @Test
    void rejectsSpecificationValuesDuplicatedAfterNormalization() {
        var request = request(List.of(
            new CreateProductRequest.SpecificationRequest(
                "Color",
                List.of("Red", " Red ")
            )
        ));

        var exception = assertThrows(
            ValidationException.class,
            () -> ProductValidator.validate(request)
        );

        assertEquals(
            "specifications[0].values[1] is duplicated",
            exception.getMessage()
        );
    }

    @Test
    void rejectsSpecificationNamesDuplicatedAfterNormalization() {
        var request = request(List.of(
            new CreateProductRequest.SpecificationRequest(
                "Color",
                List.of("Red")
            ),
            new CreateProductRequest.SpecificationRequest(
                " Color ",
                List.of("Blue")
            )
        ));

        var exception = assertThrows(
            ValidationException.class,
            () -> ProductValidator.validate(request)
        );

        assertEquals("specifications[1] is duplicated", exception.getMessage());
    }

    @Test
    void rejectsPriceWithMoreThanTwoFractionDigits() {
        var request = request(
            List.of(new CreateProductRequest.SpecificationRequest(
                "Color",
                List.of("Red")
            )),
            List.of(new CreateProductRequest.SkuRequest(
                null,
                new BigDecimal("1.001"),
                null,
                List.of(new CreateProductRequest.SelectionRequest(
                    "Color",
                    "Red"
                ))
            ))
        );

        var exception = assertThrows(
            ValidationException.class,
            () -> ProductValidator.validate(request)
        );

        assertEquals(
            "skus[0].price must have at most 2 fraction digits",
            exception.getMessage()
        );
    }

    private static CreateProductRequest request(
        List<CreateProductRequest.SpecificationRequest> specifications
    ) {
        return request(
            specifications,
            List.of(new CreateProductRequest.SkuRequest(
                " SKU-1 ",
                new BigDecimal("99.00"),
                null,
                specifications.stream()
                    .map(specification ->
                        new CreateProductRequest.SelectionRequest(
                            specification.name(),
                            specification.values().getFirst()
                        ))
                    .toList()
            ))
        );
    }

    private static CreateProductRequest request(
        List<CreateProductRequest.SpecificationRequest> specifications,
        List<CreateProductRequest.SkuRequest> skus
    ) {
        return new CreateProductRequest(
            " Product ",
            null,
            " Description ",
            " category ",
            UUID7s.generate(),
            List.of(UUID7s.generate()),
            specifications,
            skus
        );
    }
}
