package org.ayachinene.app.product.domain;

import org.ayachinene.app.product.creation.SkuInput;
import org.ayachinene.app.product.creation.SpecificationInput;
import org.ayachinene.app.exception.ValidationException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class SkuSpecificationValidator {

    private SkuSpecificationValidator() {
    }

    public static void validate(
            List<SpecificationInput> specifications,
            List<SkuInput> skus
    ) {
        if (specifications.isEmpty() && skus.size() != 1) {
            throw new ValidationException(
                    "product without specifications must contain exactly one SKU"
            );
        }

        var valuesBySpecification = valuesBySpecification(specifications);

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

    private static Map<String, Set<String>> valuesBySpecification(
            List<SpecificationInput> specifications
    ) {
        return specifications.stream().collect(Collectors.toUnmodifiableMap(
                SpecificationInput::name,
                specification -> Set.copyOf(specification.values())
        ));
    }
}
