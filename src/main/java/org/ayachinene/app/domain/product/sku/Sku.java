package org.ayachinene.app.domain.product.sku;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.exception.ValidationException;
import org.ayachinene.shared.uuid7.UUID7;

import java.util.List;

public record Sku(
        SkuCode skuCode,
        String merchantSkuCode,
        SkuStatus status,
        Money price,
        UUID7 imageFileId,
        List<SpecificationSelection> specificationSelections
) {

    public Sku {
        specificationSelections = List.copyOf(specificationSelections);
        var uniqueSpecificationCount = specificationSelections.stream()
                .map(SpecificationSelection::specificationId)
                .distinct()
                .count();
        if (uniqueSpecificationCount != specificationSelections.size()) {
            throw new ValidationException(
                    "specificationSelections must not contain duplicate specifications"
            );
        }
    }
}
