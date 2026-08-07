package org.ayachinene.app.service.product;

import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.domain.product.Products;
import org.ayachinene.app.domain.product.ProductRepository;
import org.ayachinene.app.domain.product.creation.CreateProductInput;
import org.ayachinene.app.service.Tx;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final Tx tx;

    public ProductService(
            ProductRepository productRepository,
            Tx tx
    ) {
        this.productRepository = productRepository;
        this.tx = tx;
    }

    public ProductCode createProduct(CreateProductInput input) {
        Objects.requireNonNull(input, "input must not be null");

        var newProductCode = ProductCode.generate();
        var creation = Products.create(newProductCode, input);

        tx.run(() -> productRepository.create(creation));

        return newProductCode;
    }
}
