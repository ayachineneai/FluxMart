package org.ayachinene.app.domain.product.sku;

import org.ayachinene.app.domain.file.FileResourceId;
import org.ayachinene.app.domain.money.Money;

import java.util.List;
import java.util.Objects;

public record Sku(
        SkuCode skuCode,
        String merchantSkuCode,
        SkuStatus status,
        Money price,
        FileResourceId imageFileId,
        List<SpecificationSelection> specificationSelections
) {

    public Sku {
        Objects.requireNonNull(skuCode, "skuCode must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(price, "price must not be null");
        specificationSelections = List.copyOf(
                Objects.requireNonNull(
                        specificationSelections,
                        "specificationSelections must not be null"
                )
        );

        for (var selection : specificationSelections) {
            Objects.requireNonNull(selection, "specificationSelections element must not be null");
        }
        var uniqueSpecificationCount = specificationSelections.stream()
                .map(SpecificationSelection::specificationId)
                .distinct()
                .count();
        if (uniqueSpecificationCount != specificationSelections.size()) {
            throw new IllegalArgumentException(
                    "specificationSelections must not contain duplicate specifications"
            );
        }
    }
}
