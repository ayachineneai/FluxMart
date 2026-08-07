package org.ayachinene.api.product;

import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.domain.product.creation.CreateProductInput;
import org.ayachinene.app.service.product.ProductService;
import org.ayachinene.shared.uuid7.UUID7s;
import org.ayachinene.api.ApiExceptionHandler;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class ProductControllerTest {

    @Test
    void createsProductFromHttpRequest() throws Exception {
        var productService = mock(ProductService.class);
        var productCode = ProductCode.generate();
        when(productService.createProduct(any())).thenReturn(productCode);

        mockMvc(productService).perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productCode").value(productCode.value()));

        var inputCaptor = ArgumentCaptor.forClass(CreateProductInput.class);
        verify(productService).createProduct(inputCaptor.capture());
        var input = inputCaptor.getValue();
        assertEquals("纯棉 T 恤", input.title());
        assertEquals("TSHIRT", input.categoryCode().value());
        assertEquals(1, input.specifications().size());
        assertEquals(1, input.skus().size());
    }

    @Test
    void returnsBadRequestForInvalidFileId() throws Exception {
        var productService = mock(ProductService.class);

        mockMvc(productService).perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "纯棉 T 恤",
                                  "description": "100% 纯棉",
                                  "categoryCode": "TSHIRT",
                                  "primaryImageFileId": "not-a-uuid"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    private static MockMvc mockMvc(ProductService productService) {
        return standaloneSetup(new ProductController(
                productService,
                new ProductApiMapper()
        ))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    private static String validRequest() {
        var primaryImageFileId = UUID7s.generate();
        return """
                {
                  "title": "纯棉 T 恤",
                  "subtitle": "柔软透气",
                  "description": "100%% 纯棉",
                  "categoryCode": "TSHIRT",
                  "primaryImageFileId": "%s",
                  "galleryImageFileIds": [],
                  "specifications": [
                    {"name": "颜色", "values": ["黑色"]}
                  ],
                  "skus": [
                    {
                      "merchantSkuCode": "TSHIRT-BLACK",
                      "price": 99.00,
                      "imageFileId": null,
                      "selections": [
                        {"specification": "颜色", "value": "黑色"}
                      ]
                    }
                  ]
                }
                """.formatted(primaryImageFileId);
    }
}
