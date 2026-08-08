package org.ayachinene.api.file.controller;

import org.ayachinene.api.file.FileApiMapper;
import org.ayachinene.api.file.data.PrepareProductImageUploadRequest;
import org.ayachinene.api.file.data.PrepareProductImageUploadResponse;
import org.ayachinene.api.file.data.ConfirmFileUploadResponse;
import org.ayachinene.app.file.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;
    private final FileApiMapper fileMapper;

    public FileController(
            FileService fileService,
            FileApiMapper fileMapper
    ) {
        this.fileService = fileService;
        this.fileMapper = fileMapper;
    }

    @PostMapping("/product-images/upload-sessions")
    @ResponseStatus(HttpStatus.CREATED)
    public PrepareProductImageUploadResponse prepareProductImageUpload(
            @RequestBody PrepareProductImageUploadRequest request
    ) {
        var input = fileMapper.toInput(request);
        var result = fileService.prepareProductImageUpload(input);
        return fileMapper.toResponse(result);
    }

    @PostMapping("/{fileId}/complete")
    public ConfirmFileUploadResponse confirmProductImageUpload(
            @PathVariable String fileId
    ) {
        var result = fileService.confirmProductImageUpload(
                fileMapper.toFileId(fileId)
        );
        return fileMapper.toResponse(result);
    }
}
