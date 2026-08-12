package org.ayachinene.api.file;

import org.ayachinene.api.file.controller.FileController;
import org.ayachinene.app.file.upload.FileUploadService;
import org.ayachinene.app.file.domain.FileStatus;
import org.ayachinene.app.file.upload.ConfirmedFileUpload;
import org.ayachinene.shared.uuid7.UUID7s;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class FileControllerTest {

    @Test
    void confirmsFileUploadFromPathVariable() throws Exception {
        var fileUploadService = mock(FileUploadService.class);
        var fileId = UUID7s.generate();
        when(fileUploadService.confirmUpload(fileId))
                .thenReturn(new ConfirmedFileUpload(
                        fileId,
                        FileStatus.AVAILABLE
                ));

        standaloneSetup(new FileController(
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
