package org.ayachinene.app.product.creation;

import org.ayachinene.app.product.domain.CategoryCode;
import org.ayachinene.shared.uuid7.UUID7;

import java.util.List;

public record ProductInput(
        String title,
        String subtitle,
        String description,
        CategoryCode categoryCode,
        UUID7 primaryImageFileId,
        List<UUID7> galleryImageFileIds
) {
}
