package org.ayachinene.infra.persistence.product;

import org.ayachinene.app.domain.file.FileResourceId;
import org.ayachinene.app.domain.product.Product;
import org.ayachinene.app.domain.product.ProductNotFoundException;
import org.ayachinene.app.domain.product.ProductRepository;
import org.ayachinene.app.domain.product.ProductVersionConflictException;
import org.ayachinene.app.uuid7.UUID7;
import org.ayachinene.app.uuid7.UUID7s;
import org.ayachinene.infra.persistence.product.converter.ProductPersistenceConverter;
import org.ayachinene.utils.Streams;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private static final long INITIAL_VERSION = 0L;

    private final ProductMapper productMapper;
    private final ProductGalleryImageMapper galleryImageMapper;
    private final ProductPersistenceConverter persistenceConverter;

    public ProductRepositoryImpl(
            ProductMapper productMapper,
            ProductGalleryImageMapper galleryImageMapper,
            ProductPersistenceConverter persistenceConverter
    ) {
        this.productMapper = productMapper;
        this.galleryImageMapper = galleryImageMapper;
        this.persistenceConverter = persistenceConverter;
    }

    @Override
    public void create(Product product) {
        var newProductId = UUID7s.generate();
        var createdAt = LocalDateTime.now();

        insertProduct(product, newProductId, createdAt);
        insertGalleryImages(product.galleryImageFileIds(), newProductId, createdAt);
    }

    private void insertProduct(Product product, UUID7 newProductId, LocalDateTime createdAt) {
        var productPo = persistenceConverter.toProductPo(product)
                .setId(newProductId)
                .setVersion(INITIAL_VERSION)
                .setCreatedAt(createdAt)
                .setUpdatedAt(createdAt);

        productMapper.insert(productPo);
    }

    private void insertGalleryImages(
            List<FileResourceId> galleryImageFileIds,
            UUID7 owningProductId,
            LocalDateTime createdAt
    ) {
        Streams.withIndex(galleryImageFileIds).forEach(x -> {
            var image = persistenceConverter.toGalleryImagePo(x.value())
                .setId(UUID7s.generate())
                .setProductId(owningProductId)
                .setSortOrder(x.index())
                .setCreatedAt(createdAt);
            galleryImageMapper.insert(image);
        });
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
            List<FileResourceId> galleryImageFileIds,
            UUID7 existingProductId,
            LocalDateTime createdAt
    ) {
        galleryImageMapper.deleteByProductId(existingProductId);
        insertGalleryImages(galleryImageFileIds, existingProductId, createdAt);
    }
}
