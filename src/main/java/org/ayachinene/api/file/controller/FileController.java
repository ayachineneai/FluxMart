package org.ayachinene.api.file.controller;

import org.ayachinene.api.file.FileApiMapper;
import org.ayachinene.api.file.data.ConfirmFileUploadResponse;
import org.ayachinene.app.file.upload.FileUploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileUploadService fileUploadService;
    private final FileApiMapper fileMapper;

    public FileController(
            FileUploadService fileUploadService,
            FileApiMapper fileMapper
    ) {
        this.fileUploadService = fileUploadService;
        this.fileMapper = fileMapper;
    }

    @PostMapping("/{fileId}/complete")
    public ConfirmFileUploadResponse confirmFileUpload(
            @PathVariable String fileId
    ) {
        var result = fileUploadService.confirmUpload(
                fileMapper.toFileId(fileId)
        );
        return fileMapper.toResponse(result);
    }
}
