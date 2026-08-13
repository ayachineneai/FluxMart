package org.ayachinene.app.product.creation;

import org.ayachinene.api.product.data.CreateProductRequest;
import org.ayachinene.app.product.creation.validate.ProductValidator;
import org.ayachinene.shared.exception.ValidationException;
import org.ayachinene.shared.uuid7.UUID7s;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductValidatorTest {

    @Test
    void normalizesProductAndSpecificationText() {
        var specifications = new LinkedHashMap<String, List<String>>();
        specifications.put(" Color ", List.of(" Red ", "Blue"));

        var validated = validate(request(specifications));

        assertEquals("Product", validated.title());
        assertNull(validated.subtitle());
        assertEquals("Description", validated.description());
        assertEquals("category", validated.categoryCode());
        assertEquals(List.of("Color"), List.copyOf(validated.specifications().keySet()));
        assertEquals(List.of("Red", "Blue"), validated.specifications().get("Color"));
        assertEquals("SKU-1", validated.skus().getFirst().merchantSkuCode());
    }

    @Test
    void removesSpecificationValuesDuplicatedAfterNormalization() {
        var specifications = specifications("Color", List.of("Red", " Red "));

        var validated = validate(request(specifications));

        assertEquals(List.of("Red"), validated.specifications().get("Color"));
    }

    @Test
    void rejectsSpecificationNamesDuplicatedAfterNormalization() {
        var specifications = new LinkedHashMap<String, List<String>>();
        specifications.put("Color", List.of("Red"));
        specifications.put(" Color ", List.of("Blue"));
        var request = request(
            specifications,
            List.of(sku(null, new BigDecimal("99.00"), Map.of("Color", "Red")))
        );

        var exception = assertThrows(
            ValidationException.class,
            () -> validate(request)
        );

        assertEquals("specification names must be unique", exception.getMessage());
    }

    @Test
    void rejectsPriceWithMoreThanTwoFractionDigits() {
        var request = request(
            specifications("Color", List.of("Red")),
            List.of(sku(null, new BigDecimal("1.001"), Map.of("Color", "Red")))
        );

        var exception = assertThrows(
            ValidationException.class,
            () -> validate(request)
        );

        assertEquals(
            "sku price must have at most 2 fraction digits",
            exception.getMessage()
        );
    }

    @Test
    void removesSelectionsWithoutSpecifications() {
        var request = request(
            new LinkedHashMap<>(),
            List.of(sku(
                null,
                new BigDecimal("99.00"),
                Map.of("Color", "Red")
            ))
        );

        var validated = validate(request);

        assertEquals(Map.of(), validated.skus().getFirst().selections());
    }

    private static CreateProductRequest request(
        LinkedHashMap<String, List<String>> specifications
    ) {
        var selections = new LinkedHashMap<String, String>();
        specifications.forEach((name, values) ->
            selections.put(name, values.getFirst())
        );
        return request(
            specifications,
            List.of(sku(" SKU-1 ", new BigDecimal("99.00"), selections))
        );
    }

    private static CreateProductRequest request(
        LinkedHashMap<String, List<String>> specifications,
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

    private static CreateProductRequest.SkuRequest sku(
        String merchantSkuCode,
        BigDecimal price,
        Map<String, String> selections
    ) {
        return new CreateProductRequest.SkuRequest(
            merchantSkuCode,
            price,
            null,
            selections
        );
    }

    private static LinkedHashMap<String, List<String>> specifications(
        String name,
        List<String> values
    ) {
        var specifications = new LinkedHashMap<String, List<String>>();
        specifications.put(name, values);
        return specifications;
    }

    private static CreateProductRequest validate(CreateProductRequest request) {
        return ProductValidator.validate(ProductPreprocessor.preprocess(request));
    }
}
