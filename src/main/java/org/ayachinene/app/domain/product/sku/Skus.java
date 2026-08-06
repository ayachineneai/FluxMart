package org.ayachinene.app.domain.product.sku;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.domain.product.creation.SelectionInput;
import org.ayachinene.app.domain.product.creation.SkuInput;
import org.ayachinene.app.domain.product.specification.Specification;
import org.ayachinene.app.domain.product.specification.SpecificationValue;
import org.ayachinene.app.uuid7.UUID7s;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Skus {

    private Skus() {
    }

    public static List<Sku> mkSkus(
            List<SkuInput> inputs,
            List<Specification> specifications
    ) {
        var specificationsByName = specifications.stream()
                .collect(Collectors.toUnmodifiableMap(
                        Specification::name,
                        Function.identity()
                ));
        return inputs.stream()
                .map(input -> mkSku(input, specificationsByName))
                .toList();
    }

    private static Sku mkSku(
            SkuInput input,
            Map<String, Specification> specificationsByName
    ) {
        return new Sku(
                new SkuCode(UUID7s.generate()),
                input.merchantSkuCode(),
                SkuStatus.DISABLED,
                new Money(input.price()),
                input.imageFileId(),
                input.selections().stream()
                        .map(selection -> mkSelection(selection, specificationsByName))
                        .toList()
        );
    }

    private static SpecificationSelection mkSelection(
            SelectionInput input,
            Map<String, Specification> specificationsByName
    ) {
        var specification = requireSpecification(
                specificationsByName,
                input.specification()
        );
        var value = requireValue(specification, input.value());
        return new SpecificationSelection(
                specification.specificationId(),
                value.specificationValueId()
        );
    }

    private static Specification requireSpecification(
            Map<String, Specification> specificationsByName,
            String name
    ) {
        var specification = specificationsByName.get(name);
        if (specification == null) {
            throw new IllegalArgumentException("Unknown specification: " + name);
        }
        return specification;
    }

    private static SpecificationValue requireValue(
            Specification specification,
            String displayName
    ) {
        return specification.values().stream()
                .filter(value -> value.displayName().equals(displayName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown value for specification "
                                + specification.name()
                                + ": "
                                + displayName
                ));
    }
}
