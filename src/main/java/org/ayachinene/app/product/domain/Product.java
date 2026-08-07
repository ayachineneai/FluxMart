package org.ayachinene.app.product.domain;

import org.ayachinene.shared.uuid7.UUID7;

import java.util.List;

public record Product(
        ProductCode productCode,
        ProductStatus status,
        String title,
        String subtitle,
        String description,
        CategoryCode categoryCode,
        UUID7 primaryImageFileId,
        List<UUID7> galleryImageFileIds
) {
    public Product {
        galleryImageFileIds = List.copyOf(galleryImageFileIds);
    }
}
