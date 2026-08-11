package org.ayachinene.infra.product.persistence;

import org.ayachinene.app.product.domain.Product;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.ProductNotFoundException;
import org.ayachinene.app.product.repository.ProductRepository;
import org.ayachinene.app.product.domain.ProductVersionConflictException;
import org.ayachinene.app.product.creation.ProductCreation;
import org.ayachinene.app.product.publication.ProductPublication;
import org.ayachinene.app.product.publication.ProductPublicationState;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.shared.uuid7.UUID7s;
import org.ayachinene.infra.product.persistence.converter.ProductPersistenceConverter;
import org.ayachinene.utils.Streams;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductMapper productMapper;
    private final ProductGalleryImageMapper galleryImageMapper;
    private final ProductCreationPersistenceConverter creationConverter;
    private final ProductCreationPersistenceInserter creationInserter;
    private final ProductPersistenceConverter persistenceConverter;

    public ProductRepositoryImpl(
        ProductMapper productMapper,
        ProductGalleryImageMapper galleryImageMapper,
        ProductCreationPersistenceConverter creationConverter,
        ProductCreationPersistenceInserter creationInserter,
        ProductPersistenceConverter persistenceConverter
    ) {
        this.productMapper = productMapper;
        this.galleryImageMapper = galleryImageMapper;
        this.creationConverter = creationConverter;
        this.creationInserter = creationInserter;
        this.persistenceConverter = persistenceConverter;
    }

    @Override
    public void create(ProductCreation creation) {
        var pos = creationConverter.toPos(creation);
        creationInserter.insert(pos);
    }

    private void insertGalleryImages(
        UUID7 productId,
        List<UUID7> galleryImageFileIds,
        List<UUID7> galleryImageIds,
        LocalDateTime createdAt
    ) {
        if (galleryImageFileIds.isEmpty()) return;

        var images = Streams.withIndex(galleryImageFileIds)
            .map(indexed -> persistenceConverter.toGalleryImagePo(indexed.value())
                .setId(galleryImageIds.get(indexed.index()))
                .setProductId(productId)
                .setSortOrder(indexed.index())
                .setCreatedAt(createdAt)
            )
            .toList();
        galleryImageMapper.insertBatch(images);
    }

    @Override
    public void update(Product product, long expectedVersion) {
        var existingProductId = productMapper.selectIdByProductCode(
            product.productCode()
        );

        if (existingProductId == null) {
            throw new ProductNotFoundException(product.productCode());
        }

        var updatedAt = LocalDateTime.now();
        updateProduct(product, expectedVersion, updatedAt);
        replaceGalleryImages(
            product.galleryImageFileIds(),
            existingProductId,
            updatedAt
        );
    }

    private void updateProduct(
        Product product,
        long expectedVersion,
        LocalDateTime updatedAt
    ) {
        var productPo = persistenceConverter.toProductPo(product)
            .setUpdatedAt(updatedAt);

        var affectedRows = productMapper.updateByProductCodeAndVersion(
            productPo,
            expectedVersion
        );
        if (affectedRows == 0) {
            throw new ProductVersionConflictException(
                product.productCode(),
                expectedVersion
            );
        }
    }

    private void replaceGalleryImages(
        List<UUID7> galleryImageFileIds,
        UUID7 existingProductId,
        LocalDateTime createdAt
    ) {
        var galleryImageIds = galleryImageFileIds.stream()
            .map(fileId -> UUID7s.generate())
            .toList();

        galleryImageMapper.deleteByProductId(existingProductId);
        insertGalleryImages(
            existingProductId,
            galleryImageFileIds,
            galleryImageIds,
            createdAt
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
