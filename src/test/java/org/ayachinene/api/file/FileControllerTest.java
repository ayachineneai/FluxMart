package org.ayachinene.api.file;

import org.ayachinene.api.file.controller.FileController;
import org.ayachinene.app.file.productimage.ProductImageService;
import org.ayachinene.app.file.upload.FileUploadService;
import org.ayachinene.app.file.domain.FileStatus;
import org.ayachinene.app.file.storage.UploadAuthorization;
import org.ayachinene.app.file.storage.UploadFormFields;
import org.ayachinene.app.file.productimage.PrepareProductImageUploadInput;
import org.ayachinene.app.file.upload.PreparedFileUpload;
import org.ayachinene.app.file.upload.ConfirmedFileUpload;
import org.ayachinene.shared.uuid7.UUID7s;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class FileControllerTest {

    @Test
    void preparesProductImageUploadFromHttpRequest() throws Exception {
        var productImageService = mock(ProductImageService.class);
        var fileUploadService = mock(FileUploadService.class);
        var fileId = UUID7s.generate();
        var expiresAt = OffsetDateTime.parse("2026-08-08T12:05:00+08:00");
        when(productImageService.prepareUpload(any()))
                .thenReturn(new PreparedFileUpload(
                        fileId,
                        FileStatus.UPLOADING,
                        new UploadAuthorization(
                                "https://fluxmart.oss-cn-shanghai.aliyuncs.com",
                                new UploadFormFields(
                                        "product-images/" + fileId,
                                        "image/png",
                                        "201",
                                        "true",
                                        "policy",
                                        "OSS4-HMAC-SHA256",
                                        "credential",
                                        "20260808T040000Z",
                                        "signature"
                                )
                        ),
                        expiresAt
                ));

        standaloneSetup(new FileController(
                productImageService,
                fileUploadService,
                new FileApiMapper()
        ))
                .build()
                .perform(post("/api/files/product-images/upload-sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "filename": "black-shirt.png",
                                  "contentType": "image/png",
                                  "sizeInBytes": 183421
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fileId").value(fileId.toString()))
                .andExpect(jsonPath("$.status").value("UPLOADING"))
                .andExpect(jsonPath("$.upload.fields.key")
                        .value("product-images/" + fileId))
                .andExpect(jsonPath("$.upload.fields['Content-Type']")
                        .value("image/png"))
                .andExpect(jsonPath("$.upload.fields['x-oss-signature']")
                        .value("signature"));

        var inputCaptor = ArgumentCaptor.forClass(
                PrepareProductImageUploadInput.class
        );
        verify(productImageService).prepareUpload(inputCaptor.capture());
        assertEquals("black-shirt.png", inputCaptor.getValue().filename());
        assertEquals("image/png", inputCaptor.getValue().contentType());
        assertEquals(183421L, inputCaptor.getValue().sizeInBytes());
    }

    @Test
    void confirmsProductImageUploadFromPathVariable() throws Exception {
        var productImageService = mock(ProductImageService.class);
        var fileUploadService = mock(FileUploadService.class);
        var fileId = UUID7s.generate();
        when(fileUploadService.confirmUpload(fileId))
                .thenReturn(new ConfirmedFileUpload(
                        fileId,
                        FileStatus.AVAILABLE
                ));

        standaloneSetup(new FileController(
                productImageService,
                fileUploadService,
                new FileApiMapper()
        ))
                .build()
                .perform(post("/api/files/{fileId}/complete", fileId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileId").value(fileId.toString()))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));

        verify(fileUploadService).confirmUpload(fileId);
    }
}
