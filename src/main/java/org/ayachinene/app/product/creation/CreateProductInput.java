package org.ayachinene.app.product.creation;

import org.ayachinene.app.product.domain.CategoryCode;
import org.ayachinene.shared.uuid7.UUID7;

import java.math.BigDecimal;
import java.util.List;

public record CreateProductInput(
        String title,
        String subtitle,
        String description,
        CategoryCode categoryCode,
        UUID7 primaryImageFileId,
        List<UUID7> galleryImageFileIds,
        List<Specification> specifications,
        List<Sku> skus
) {

    public record Specification(
            String name,
            List<String> values
    ) {
    }

    public record Sku(
            String merchantSkuCode,
            BigDecimal price,
            UUID7 imageFileId,
            List<Selection> selections
    ) {
    }

    public record Selection(
            String specification,
            String value
    ) {
    }
}
