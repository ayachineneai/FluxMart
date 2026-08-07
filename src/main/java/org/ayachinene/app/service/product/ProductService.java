package org.ayachinene.app.service.product;

import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.domain.product.Products;
import org.ayachinene.app.domain.product.ProductRepository;
import org.ayachinene.app.domain.product.creation.CreateProductInput;
import org.ayachinene.app.domain.product.publication.PublishProductInput;
import org.ayachinene.app.domain.product.publication.PublishProductResult;
import org.ayachinene.app.domain.product.sku.SkuRepository;
import org.ayachinene.app.service.Tx;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SkuRepository skuRepository;
    private final Tx tx;

    public ProductService(
            ProductRepository productRepository,
            SkuRepository skuRepository,
            Tx tx
    ) {
        this.productRepository = productRepository;
        this.skuRepository = skuRepository;
        this.tx = tx;
    }

    public ProductCode createProduct(CreateProductInput input) {
        var creation = Products.create(input);

        tx.run(() -> productRepository.create(creation));

        return creation.product().productCode();
    }

    public PublishProductResult publishProduct(PublishProductInput input) {
        return tx.run(() -> {
            var publicationState = productRepository.findPublicationState(
                    input.productCode()
            );
            var hasSku = skuRepository.existsByProductCode(
                    input.productCode()
            );
            var publication = Products.publish(publicationState, hasSku);
            var publishedVersion = productRepository.publish(
                    publication,
                    input.expectedVersion()
            );
            return new PublishProductResult(
                    publication.productCode(),
                    publication.status(),
                    publishedVersion
            );
        });
    }
}
