package org.ayachinene.app.product.creation.validate;

import org.ayachinene.api.product.data.CreateProductRequest;
import org.ayachinene.utils.BigDecimals;
import org.ayachinene.utils.Lists;
import org.ayachinene.utils.Streams;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.ayachinene.utils.Validates.notNull;
import static org.ayachinene.utils.Validates.optionalText;
import static org.ayachinene.utils.Validates.require;
import static org.ayachinene.utils.Validates.text;

public final class SkuValidator {

    private static final BigDecimal MAX_PRICE = new BigDecimal("99999999.99");

    private SkuValidator() {
    }

    public static void validate(
        List<CreateProductRequest.SkuRequest> skus
    ) {
        require(!skus.isEmpty(), "skus must not be empty");
        skus.forEach(SkuValidator::sku);
        require(
            Lists.isUnique(
                Streams.of(skus)
                    .map(CreateProductRequest.SkuRequest::merchantSkuCode)
                    .filter(Objects::nonNull)
                    .toList()
            ),
            "merchantSkuCode must be unique"
        );
    }

    private static void sku(CreateProductRequest.SkuRequest sku) {
        optionalText(sku.merchantSkuCode(), "merchant sku code", 64);
        price(sku.price());
        sku.selections().forEach((specification, value) -> {
            text(specification, "selection specification", 50);
            text(value, "selection value", 50);
        });
    }

    private static void price(BigDecimal price) {
        notNull(price, "sku price");
        require(
            BigDecimals.isPositive(price),
            "sku price must be greater than 0"
        );
        require(
            BigDecimals.lte(price, MAX_PRICE),
            "sku price must be less than or equal to " + MAX_PRICE
        );
        require(
            !BigDecimals.hasMoreFractionDigitsThan(price, 2),
            "sku price must have at most 2 fraction digits"
        );
    }
}
