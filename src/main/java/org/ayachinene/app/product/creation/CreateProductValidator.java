package org.ayachinene.app.product.creation;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.exception.ValidationException;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.utils.BigDecimals;
import org.ayachinene.utils.Lists;
import org.ayachinene.utils.Streams;
import org.ayachinene.utils.Validates;
import org.ayachinene.utils.Values;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class CreateProductValidator {

    private static final int MAX_TITLE_LENGTH = 50;
    private static final int MAX_SUBTITLE_LENGTH = 50;
    private static final int MAX_SPECIFICATION_NAME_LENGTH = 50;
    private static final int MAX_MERCHANT_SKU_CODE_LENGTH = 64;

    private CreateProductValidator() {
    }

    public static CreateProductInput validate(CreateProductInput input) {
        var specifications = specifications(input.specifications());
        var skus = skus(input.skus());
        validateSkuSpecifications(specifications, skus);

        return new CreateProductInput(
                Validates.requiredText(input.title(), "title", MAX_TITLE_LENGTH),
                Values.map(input.subtitle(), subtitle -> Validates.requiredText(
                        subtitle,
                        "subtitle",
                        MAX_SUBTITLE_LENGTH
                )),
                Validates.requiredText(input.description(), "description"),
                Validates.requireNonNull(input.categoryCode(), "categoryCode"),
                fileId(input.primaryImageFileId(), "primaryImageFileId"),
                galleryImageFileIds(input.galleryImageFileIds()),
                specifications,
                skus
        );
    }

    private static UUID7 fileId(UUID7 value, String field) {
        return Validates.requireNonNull(value, field);
    }

    private static List<UUID7> galleryImageFileIds(List<UUID7> values) {
        var normalized = Streams.of(values)
                .map(value -> fileId(value, "galleryImageFileIds element"))
                .toList();
        Validates.require(
                Lists.isUnique(normalized),
                "galleryImageFileIds must not contain duplicates"
        );
        return normalized;
    }

    private static List<CreateProductInput.Specification> specifications(
            List<CreateProductInput.Specification> inputs
    ) {
        var specifications = Streams.of(inputs)
                .map(CreateProductValidator::specification)
                .toList();
        Validates.require(
                Lists.isUnique(
                        specifications,
                        CreateProductInput.Specification::name
                ),
                "specification names must not be duplicated"
        );
        return specifications;
    }

    private static CreateProductInput.Specification specification(
            CreateProductInput.Specification input
    ) {
        Validates.requireNonNull(input, "specifications element");
        var name = Validates.requiredText(
                input.name(),
                "specification name",
                MAX_SPECIFICATION_NAME_LENGTH
        );
        var values = Streams.of(input.values())
                .map(value -> Validates.requiredText(
                        value,
                        "specification value",
                        MAX_SPECIFICATION_NAME_LENGTH
                ))
                .toList();
        Validates.require(
                !values.isEmpty(),
                "specification values must not be empty: " + name
        );
        Validates.require(
                Lists.isUnique(values),
                "specification values must not be duplicated: " + name
        );
        return new CreateProductInput.Specification(name, values);
    }

    private static List<CreateProductInput.Sku> skus(
            List<CreateProductInput.Sku> inputs
    ) {
        var skus = Streams.of(inputs)
                .map(CreateProductValidator::sku)
                .toList();
        Validates.require(!skus.isEmpty(), "skus must not be empty");
        Validates.require(
                Lists.isUnique(skus.stream()
                        .map(CreateProductInput.Sku::merchantSkuCode)
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

    private static CreateProductInput.Sku sku(
            CreateProductInput.Sku input
    ) {
        Validates.requireNonNull(input, "skus element");
        return new CreateProductInput.Sku(
                Values.map(input.merchantSkuCode(), value -> Validates.requiredText(
                        value,
                        "merchantSkuCode",
                        MAX_MERCHANT_SKU_CODE_LENGTH
                )),
                price(input.price()),
                input.imageFileId(),
                selections(input.selections())
        );
    }

    private static BigDecimal price(BigDecimal value) {
        Validates.requireNonNull(value, "price");
        return Values.filter(Money.validate(value), BigDecimals::isPositive)
                .getOrElseThrow(() -> new ValidationException("price must be positive"));
    }

    private static List<CreateProductInput.Selection> selections(
            List<CreateProductInput.Selection> inputs
    ) {
        var selections = Streams.of(inputs)
                .map(CreateProductValidator::selection)
                .toList();
        Validates.require(
                Lists.isUnique(
                        selections,
                        CreateProductInput.Selection::specification
                ),
                "SKU must not select the same specification more than once"
        );
        return selections;
    }

    private static CreateProductInput.Selection selection(
            CreateProductInput.Selection input
    ) {
        Validates.requireNonNull(input, "selections element");
        return new CreateProductInput.Selection(
                Validates.requiredText(
                        input.specification(),
                        "selection specification"
                ),
                Validates.requiredText(input.value(), "selection value")
        );
    }

    private static Map<String, String> configurationOf(
            List<CreateProductInput.Selection> selections
    ) {
        return selections.stream().collect(Collectors.toUnmodifiableMap(
                CreateProductInput.Selection::specification,
                CreateProductInput.Selection::value
        ));
    }

    private static void validateSkuSpecifications(
            List<CreateProductInput.Specification> specifications,
            List<CreateProductInput.Sku> skus
    ) {
        if (specifications.isEmpty() && skus.size() != 1) {
            throw new ValidationException(
                    "product without specifications must contain exactly one SKU"
            );
        }

        var valuesBySpecification = specifications.stream()
                .collect(Collectors.toUnmodifiableMap(
                        CreateProductInput.Specification::name,
                        specification -> Set.copyOf(specification.values())
                ));
        for (var sku : skus) {
            if (sku.selections().size() != specifications.size()) {
                throw new ValidationException(
                        "each SKU must select exactly one value for every specification"
                );
            }
            for (var selection : sku.selections()) {
                var values = valuesBySpecification.get(selection.specification());
                if (values == null) {
                    throw new ValidationException(
                            "SKU selects an undefined specification: "
                                    + selection.specification()
                    );
                }
                if (!values.contains(selection.value())) {
                    throw new ValidationException(
                            "SKU selects an undefined value for specification "
                                    + selection.specification()
                                    + ": "
                                    + selection.value()
                    );
                }
            }
        }
    }
}
