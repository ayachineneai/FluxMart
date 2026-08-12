package org.ayachinene.app.product;

import org.ayachinene.api.product.data.CreateProductRequest;
import org.ayachinene.app.product.creation.ProductValidator;
import org.ayachinene.app.product.creation.ProductPosCreator;
import org.ayachinene.app.product.repository.ProductRepository;
import org.ayachinene.app.service.Tx;
import org.springframework.stereotype.Service;

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

    public void create(CreateProductRequest request) {
        var validated = ProductValidator.validate(request);
        var pos = ProductPosCreator.mkPos(validated);
        tx.run(() -> productRepository.insert(pos));
    }
}
