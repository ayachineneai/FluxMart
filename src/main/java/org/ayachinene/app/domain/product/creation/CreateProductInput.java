package org.ayachinene.app.domain.product.creation;

import org.ayachinene.app.domain.product.CategoryCode;
import org.ayachinene.shared.uuid7.UUID7;

import java.util.List;

public record CreateProductInput(
        String title,
        String subtitle,
        String description,
        CategoryCode categoryCode,
        UUID7 primaryImageFileId,
        List<UUID7> galleryImageFileIds,
        List<SpecificationInput> specifications,
        List<SkuInput> skus
) {

    public CreateProductInput(
            String title,
            String subtitle,
            String description,
            CategoryCode categoryCode,
            UUID7 primaryImageFileId,
            List<UUID7> galleryImageFileIds
    ) {
        this(
                title,
                subtitle,
                description,
                categoryCode,
                primaryImageFileId,
                galleryImageFileIds,
                List.of(),
                List.of()
        );
    }
}
