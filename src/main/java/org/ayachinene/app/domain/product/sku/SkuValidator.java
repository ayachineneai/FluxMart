package org.ayachinene.app.domain.product.sku;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.domain.product.creation.SelectionInput;
import org.ayachinene.app.domain.product.creation.SkuInput;
import org.ayachinene.app.exception.ValidationException;
import org.ayachinene.utils.BigDecimals;
import org.ayachinene.utils.Lists;
import org.ayachinene.utils.Streams;
import org.ayachinene.utils.Validates;
import org.ayachinene.utils.Values;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class SkuValidator {

    private static final int MAX_MERCHANT_SKU_CODE_LENGTH = 64;

    private SkuValidator() {
    }

    public static List<SkuInput> validate(List<SkuInput> inputs) {
        var skus = Streams.of(inputs)
                .map(SkuValidator::validate)
                .toList();

        Validates.require(!skus.isEmpty(), "skus must not be empty");
        Validates.require(
                Lists.isUnique(skus.stream()
                        .map(SkuInput::merchantSkuCode)
                        .filter(Objects::nonNull)
                        .toList()),
                "merchantSkuCode must not be duplicated"
        );
        Validates.require(
                Lists.isUnique(skus, sku -> configurationOf(sku.selections())),
                "SKU configurations must not be duplicated"
        );
        return skus;
    }

    private static SkuInput validate(SkuInput input) {
        Validates.requireNonNull(input, "skus element");
        return new SkuInput(
                merchantSkuCode(input.merchantSkuCode()),
                price(input.price()),
                input.imageFileId(),
                selections(input.selections())
        );
    }

    private static String merchantSkuCode(String value) {
        return Values.map(
                value,
                x -> Validates.requiredText(
                        x,
                        "merchantSkuCode",
                        MAX_MERCHANT_SKU_CODE_LENGTH
                )
        );
    }

    private static BigDecimal price(BigDecimal value) {
        Validates.requireNonNull(value, "price");
        return Values.filter(Money.validate(value), BigDecimals::isPositive)
                .getOrElseThrow(() -> new ValidationException("price must be positive"));
    }

    private static List<SelectionInput> selections(List<SelectionInput> inputs) {
        var selections = Streams.of(inputs)
                .map(SkuValidator::selection)
                .toList();
        Validates.require(
                Lists.isUnique(selections, SelectionInput::specification),
                "SKU must not select the same specification more than once"
        );
        return selections;
    }

    private static SelectionInput selection(SelectionInput input) {
        Validates.requireNonNull(input, "selections element");
        return new SelectionInput(
                Validates.requiredText(
                        input.specification(),
                        "selection specification"
                ),
                Validates.requiredText(input.value(), "selection value")
        );
    }

    private static Map<String, String> configurationOf(List<SelectionInput> selections) {
        return selections.stream().collect(Collectors.toUnmodifiableMap(
                SelectionInput::specification,
                SelectionInput::value
        ));
    }

}
