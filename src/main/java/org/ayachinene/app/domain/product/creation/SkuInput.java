package org.ayachinene.app.domain.product.creation;

import org.ayachinene.shared.uuid7.UUID7;

import java.math.BigDecimal;
import java.util.List;

public record SkuInput(
        String merchantSkuCode,
        BigDecimal price,
        UUID7 imageFileId,
        List<SelectionInput> selections
) {
}
