package org.ayachinene.app.domain.product.creation;

import org.ayachinene.app.domain.file.FileResourceId;

import java.math.BigDecimal;
import java.util.List;

public record SkuInput(
        String merchantSkuCode,
        BigDecimal price,
        FileResourceId imageFileId,
        List<SelectionInput> selections
) {
}
