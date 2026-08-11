package org.ayachinene.infra.product.persistence;

import org.ayachinene.app.product.creation.CreateProductInput;
import org.ayachinene.app.product.creation.CreatedProduct;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.ProductNotFoundException;
import org.ayachinene.app.product.repository.ProductRepository;
import org.ayachinene.app.product.domain.ProductVersionConflictException;
import org.ayachinene.app.product.publication.ProductPublication;
import org.ayachinene.app.product.publication.ProductPublicationState;
import org.ayachinene.infra.product.persistence.sku.SkuPO;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductMapper productMapper;
    private final ProductCreationPOFactory creationPOFactory;
    private final ProductCreationPersistenceInserter creationInserter;

    public ProductRepositoryImpl(
        ProductMapper productMapper,
        ProductCreationPOFactory creationPOFactory,
        ProductCreationPersistenceInserter creationInserter
    ) {
        this.productMapper = productMapper;
        this.creationPOFactory = creationPOFactory;
        this.creationInserter = creationInserter;
    }

    @Override
    public CreatedProduct create(CreateProductInput input) {
        var pos = creationPOFactory.toPos(input);
        creationInserter.insert(pos);
        return new CreatedProduct(
            pos.product().getProductCode(),
            pos.skus().stream().map(SkuPO::getSkuCode).toList()
        );
    }

    @Override
    public ProductPublicationState queryPublicationState(
        ProductCode productCode
    ) {
        var product = productMapper.queryPublicationStateByProductCode(productCode);
        if (product == null) {
            throw new ProductNotFoundException(productCode);
        }
        return new ProductPublicationState(
            product.getProductCode(),
            product.getStatus(),
            product.getVersion()
        );
    }

    @Override
    public long publish(
        ProductPublication publication,
        long expectedVersion
    ) {
        var updatedAt = LocalDateTime.now();
        var affectedRows = productMapper.updateStatusByProductCodeAndVersion(
            publication.productCode(),
            publication.status(),
            expectedVersion,
            updatedAt
        );
        if (affectedRows == 0) {
            throw new ProductVersionConflictException(
                publication.productCode(),
                expectedVersion
            );
        }
        return expectedVersion + 1;
    }
}
