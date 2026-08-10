package org.ayachinene.app.product;

import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.Products;
import org.ayachinene.app.product.repository.ProductRepository;
import org.ayachinene.app.product.creation.CreateProductInput;
import org.ayachinene.app.product.publication.PublishProductInput;
import org.ayachinene.app.product.publication.PublishProductResult;
import org.ayachinene.app.product.repository.SkuRepository;
import org.ayachinene.app.service.Tx;
import org.ayachinene.app.stock.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SkuRepository skuRepository;
    private final StockRepository stockRepository;
    private final Tx tx;

    public ProductService(
            ProductRepository productRepository,
            SkuRepository skuRepository,
            StockRepository stockRepository,
            Tx tx
    ) {
        this.productRepository = productRepository;
        this.skuRepository = skuRepository;
        this.stockRepository = stockRepository;
        this.tx = tx;
    }

    public ProductCode createProduct(CreateProductInput input) {
        var creation = Products.create(input);

        tx.run(() -> {
            productRepository.create(creation);
            stockRepository.initialize(creation.skus().stream()
                    .map(sku -> sku.skuCode())
                    .toList());
        });

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
            var publication = Products.publish(
                    publicationState,
                    input.expectedVersion(),
                    hasSku
            );
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
