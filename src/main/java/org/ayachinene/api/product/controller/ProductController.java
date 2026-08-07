package org.ayachinene.api.product.controller;

import org.ayachinene.api.product.ProductApiMapper;
import org.ayachinene.api.product.data.CreateProductRequest;
import org.ayachinene.api.product.data.CreateProductResponse;
import org.ayachinene.app.service.product.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductApiMapper productMapper;

    public ProductController(
            ProductService productService,
            ProductApiMapper productMapper
    ) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateProductResponse createProduct(@RequestBody CreateProductRequest request) {
        var input = productMapper.toInput(request);
        var productCode = productService.createProduct(input);
        return new CreateProductResponse(productCode.value());
    }
}
