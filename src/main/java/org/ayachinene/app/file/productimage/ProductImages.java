package org.ayachinene.app.file.productimage;

import org.ayachinene.app.file.domain.FilePurpose;
import org.ayachinene.app.file.upload.FileUploadDefinition;

public final class ProductImages {

    private static final String OBJECT_KEY_PREFIX = "product-images/";

    private ProductImages() {
    }

    public static FileUploadDefinition defineUpload(
            PrepareProductImageUploadInput input
    ) {
        var validatedInput = ProductImageValidator.validate(input);
        return new FileUploadDefinition(
                OBJECT_KEY_PREFIX,
                validatedInput.filename(),
                validatedInput.contentType(),
                validatedInput.sizeInBytes(),
                FilePurpose.PRODUCT_IMAGE
        );
    }
}
