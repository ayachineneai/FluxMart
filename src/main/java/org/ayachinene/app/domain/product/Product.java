package org.ayachinene.app.domain.product;

import org.ayachinene.app.domain.file.FileResourceId;

import java.util.List;
import java.util.Objects;

public record Product(
        ProductCode productCode,
        ProductStatus status,
        String title,
        String subtitle,
        String description,
        CategoryCode categoryCode,
        FileResourceId primaryImageFileId,
        List<FileResourceId> galleryImageFileIds
) {
    public Product {
        Objects.requireNonNull(productCode, "productCode must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(description, "description must not be null");
        Objects.requireNonNull(categoryCode, "categoryCode must not be null");
        Objects.requireNonNull(primaryImageFileId, "primaryImageFileId must not be null");
        galleryImageFileIds = List.copyOf(
                Objects.requireNonNull(galleryImageFileIds, "galleryImageFileIds must not be null")
        );
    }
}
