package org.ayachinene.app.file.productimage;

import org.ayachinene.app.file.domain.FilePurpose;
import org.ayachinene.app.file.domain.NewFileResource;

public final class ProductImages {

    private static final String OBJECT_KEY_PREFIX = "product-images/";

    private ProductImages() {
    }

    public static NewFileResource defineUpload(
            PrepareProductImageUploadInput input
    ) {
        var validatedInput = ProductImageValidator.validate(input);
        return new NewFileResource(
                OBJECT_KEY_PREFIX,
                validatedInput.filename(),
                validatedInput.contentType(),
                validatedInput.sizeInBytes(),
                FilePurpose.PRODUCT_IMAGE
        );
    }
}
