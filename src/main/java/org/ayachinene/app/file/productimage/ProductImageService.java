package org.ayachinene.app.file.productimage;

import org.ayachinene.app.file.upload.FileUploadService;
import org.ayachinene.app.file.upload.PreparedFileUpload;
import org.springframework.stereotype.Service;

@Service
public class ProductImageService {

    private final FileUploadService fileUploadService;

    public ProductImageService(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    public PreparedFileUpload prepareUpload(
            PrepareProductImageUploadInput input
    ) {
        return fileUploadService.prepareUpload(
                ProductImages.defineUpload(input)
        );
    }
}
