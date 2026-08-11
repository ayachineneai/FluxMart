package org.ayachinene.infra.product.persistence;

import org.ayachinene.app.product.domain.CategoryCode;
import org.ayachinene.app.product.domain.Product;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.ProductNotFoundException;
import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.app.product.domain.ProductVersionConflictException;
import org.ayachinene.app.product.creation.ProductCreation;
import org.ayachinene.app.product.publication.ProductPublication;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.shared.uuid7.UUID7s;
import org.ayachinene.infra.product.persistence.converter.ProductPersistenceConverter;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductRepositoryImplTest {

    @Test
    void delegatesProductCreationToWriter() {
        var productMapper = mock(ProductMapper.class);
        var galleryMapper = mock(ProductGalleryImageMapper.class);
        var creationWriter = mock(ProductCreationWriter.class);
        var creation = mock(ProductCreation.class);

        new ProductRepositoryImpl(
                productMapper,
                galleryMapper,
                creationWriter,
                Mappers.getMapper(ProductPersistenceConverter.class)
        ).create(creation);

        verify(creationWriter).insert(creation);
    }

    @Test
    @SuppressWarnings("unchecked")
    void updatesProductByVersionAndReplacesGalleryImages() {
        var productMapper = mock(ProductMapper.class);
        var galleryMapper = mock(ProductGalleryImageMapper.class);
        var existingProductId = UUID7s.generate();
        when(productMapper.selectIdByProductCode(any()))
                .thenReturn(existingProductId);
        when(productMapper.updateByProductCodeAndVersion(any(ProductPO.class), eq(3L)))
                .thenReturn(1);

        var galleryImage = UUID7s.generate();
        var product = productWithGallery(List.of(galleryImage));

        repository(productMapper, galleryMapper).update(product, 3L);

        var productCaptor = ArgumentCaptor.forClass(ProductPO.class);
        verify(productMapper).updateByProductCodeAndVersion(productCaptor.capture(), eq(3L));
        assertEquals(product.productCode(), productCaptor.getValue().getProductCode());
        assertNotNull(productCaptor.getValue().getUpdatedAt());
        verify(galleryMapper).deleteByProductId(existingProductId);

        var galleryCaptor = ArgumentCaptor.forClass(List.class);
        verify(galleryMapper).insertBatch(galleryCaptor.capture());
        var savedImage = (ProductGalleryImagePO) galleryCaptor.getValue().getFirst();
        assertEquals(existingProductId, savedImage.getProductId());
        assertEquals(galleryImage, savedImage.getFileId());
        assertEquals(0, savedImage.getSortOrder());
    }

    @Test
    void doesNotReplaceGalleryImagesWhenVersionConflicts() {
        var productMapper = mock(ProductMapper.class);
        var galleryMapper = mock(ProductGalleryImageMapper.class);
        when(productMapper.selectIdByProductCode(any()))
                .thenReturn(UUID7s.generate());
        when(productMapper.updateByProductCodeAndVersion(any(ProductPO.class), eq(3L)))
                .thenReturn(0);

        var product = productWithGallery(List.of(UUID7s.generate()));

        assertThrows(
                ProductVersionConflictException.class,
                () -> repository(productMapper, galleryMapper).update(product, 3L)
        );
        verify(galleryMapper, never()).deleteByProductId(any());
        verify(galleryMapper, never()).insertBatch(any());
    }

    @Test
    void publishesProductByVersionAndReturnsTheNewVersion() {
        var productMapper = mock(ProductMapper.class);
        var productCode = ProductCode.generate();
        when(productMapper.updateStatusByProductCodeAndVersion(
                eq(productCode),
                eq(ProductStatus.ON_SALE),
                eq(3L),
                any(LocalDateTime.class)
        )).thenReturn(1);

        var version = repository(productMapper, mock(ProductGalleryImageMapper.class))
                .publish(
                        new ProductPublication(productCode, ProductStatus.ON_SALE),
                        3L
                );

        assertEquals(4L, version);
    }

    @Test
    void rejectsPublishingWhenVersionConflicts() {
        var productMapper = mock(ProductMapper.class);
        var productCode = ProductCode.generate();
        when(productMapper.updateStatusByProductCodeAndVersion(
                eq(productCode),
                eq(ProductStatus.ON_SALE),
                eq(3L),
                any(LocalDateTime.class)
        )).thenReturn(0);

        assertThrows(
                ProductVersionConflictException.class,
                () -> repository(productMapper, mock(ProductGalleryImageMapper.class))
                        .publish(
                                new ProductPublication(productCode, ProductStatus.ON_SALE),
                                3L
                        )
        );
    }

    @Test
    void findsTheCurrentPublicationState() {
        var productMapper = mock(ProductMapper.class);
        var productCode = ProductCode.generate();
        when(productMapper.queryPublicationStateByProductCode(productCode))
                .thenReturn(new ProductPO()
                        .setProductCode(productCode)
                        .setStatus(ProductStatus.OFF_SALE)
                        .setVersion(3L));

        var state = repository(productMapper, mock(ProductGalleryImageMapper.class))
                .queryPublicationState(productCode);

        assertEquals(productCode, state.productCode());
        assertEquals(ProductStatus.OFF_SALE, state.status());
        assertEquals(3L, state.version());
    }

    @Test
    void rejectsFindingPublicationStateForUnknownProduct() {
        var productMapper = mock(ProductMapper.class);
        var productCode = ProductCode.generate();

        assertThrows(
                ProductNotFoundException.class,
                () -> repository(productMapper, mock(ProductGalleryImageMapper.class))
                        .queryPublicationState(productCode)
        );
    }

    private static Product productWithGallery(List<UUID7> galleryImages) {
        return new Product(
                ProductCode.generate(),
                ProductStatus.DRAFT,
                "Product",
                null,
                "Description",
                new CategoryCode("CATEGORY"),
                UUID7s.generate(),
                galleryImages
        );
    }

    private static ProductRepositoryImpl repository(
            ProductMapper productMapper,
            ProductGalleryImageMapper galleryMapper
    ) {
        return new ProductRepositoryImpl(
                productMapper,
                galleryMapper,
                mock(ProductCreationWriter.class),
                Mappers.getMapper(ProductPersistenceConverter.class)
        );
    }
}
