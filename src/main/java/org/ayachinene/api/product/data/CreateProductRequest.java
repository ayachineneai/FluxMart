package org.ayachinene.api.product.data;

import org.ayachinene.shared.uuid7.UUID7;

import java.math.BigDecimal;
import java.util.List;

public record CreateProductRequest(
    String title,
    String subtitle,
    String description,
    String categoryCode,
    UUID7 primaryImageFileId,
    List<UUID7> galleryImageFileIds,
    List<SpecificationRequest> specifications,
    List<SkuRequest> skus
) {

    public record SpecificationRequest(
        String name,
        List<String> values
    ) {
    }

    public record SkuRequest(
        String merchantSkuCode,
        BigDecimal price,
        UUID7 imageFileId,
        List<SelectionRequest> selections
    ) {
    }

    public record SelectionRequest(
        String specification,
        String value
    ) {
    }
}
