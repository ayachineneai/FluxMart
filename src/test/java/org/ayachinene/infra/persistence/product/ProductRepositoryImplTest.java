package org.ayachinene.infra.persistence.product;

import org.ayachinene.app.domain.file.FileResourceId;
import org.ayachinene.app.domain.product.CategoryCode;
import org.ayachinene.app.domain.product.Product;
import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.domain.product.ProductStatus;
import org.ayachinene.app.domain.product.ProductVersionConflictException;
import org.ayachinene.app.uuid7.UUID7s;
import org.ayachinene.infra.persistence.product.converter.ProductPersistenceConverter;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductRepositoryImplTest {

    @Test
    void insertsProductAndOrderedGalleryImages() {
        var productMapper = mock(ProductMapper.class);
        var galleryMapper = mock(ProductGalleryImageMapper.class);
        when(productMapper.insert(any(ProductPO.class))).thenReturn(1);
        when(galleryMapper.insert(any(ProductGalleryImagePO.class))).thenReturn(1);

        var firstImage = new FileResourceId(UUID7s.generate());
        var secondImage = new FileResourceId(UUID7s.generate());
        var product = new Product(
                new ProductCode(UUID7s.generate()),
                ProductStatus.DRAFT,
                "Product",
                null,
                "Description",
                new CategoryCode("CATEGORY"),
                new FileResourceId(UUID7s.generate()),
                List.of(firstImage, secondImage)
        );

        new ProductRepositoryImpl(
                productMapper,
                galleryMapper,
                Mappers.getMapper(ProductPersistenceConverter.class)
        ).create(product);

        var productCaptor = ArgumentCaptor.forClass(ProductPO.class);
        verify(productMapper).insert(productCaptor.capture());
        var savedProduct = productCaptor.getValue();
        assertNotNull(savedProduct.getId());
        assertEquals(product.productCode(), savedProduct.getProductCode());
        assertEquals(ProductStatus.DRAFT, savedProduct.getStatus());
        assertEquals(0L, savedProduct.getVersion());

        var galleryCaptor = ArgumentCaptor.forClass(ProductGalleryImagePO.class);
        verify(galleryMapper, times(2)).insert(galleryCaptor.capture());
        var savedImages = galleryCaptor.getAllValues();
        assertEquals(savedProduct.getId(), savedImages.get(0).getProductId());
        assertEquals(firstImage, savedImages.get(0).getFileId());
        assertEquals(0, savedImages.get(0).getSortOrder());
        assertEquals(secondImage, savedImages.get(1).getFileId());
        assertEquals(1, savedImages.get(1).getSortOrder());
    }

    @Test
    void updatesProductByVersionAndReplacesGalleryImages() {
        var productMapper = mock(ProductMapper.class);
        var galleryMapper = mock(ProductGalleryImageMapper.class);
        var existingProductId = UUID7s.generate();
        when(productMapper.selectIdByProductCode(any()))
                .thenReturn(existingProductId);
        when(productMapper.updateByProductCodeAndVersion(any(ProductPO.class), eq(3L)))
                .thenReturn(1);
        when(galleryMapper.insert(any(ProductGalleryImagePO.class))).thenReturn(1);

        var galleryImage = new FileResourceId(UUID7s.generate());
        var product = productWithGallery(List.of(galleryImage));

        repository(productMapper, galleryMapper).update(product, 3L);

        var productCaptor = ArgumentCaptor.forClass(ProductPO.class);
        verify(productMapper).updateByProductCodeAndVersion(productCaptor.capture(), eq(3L));
        assertEquals(product.productCode(), productCaptor.getValue().getProductCode());
        assertNotNull(productCaptor.getValue().getUpdatedAt());
        verify(galleryMapper).deleteByProductId(existingProductId);

        var galleryCaptor = ArgumentCaptor.forClass(ProductGalleryImagePO.class);
        verify(galleryMapper).insert(galleryCaptor.capture());
        assertEquals(existingProductId, galleryCaptor.getValue().getProductId());
        assertEquals(galleryImage, galleryCaptor.getValue().getFileId());
        assertEquals(0, galleryCaptor.getValue().getSortOrder());
    }

    @Test
    void doesNotReplaceGalleryImagesWhenVersionConflicts() {
        var productMapper = mock(ProductMapper.class);
        var galleryMapper = mock(ProductGalleryImageMapper.class);
        when(productMapper.selectIdByProductCode(any()))
                .thenReturn(UUID7s.generate());
        when(productMapper.updateByProductCodeAndVersion(any(ProductPO.class), eq(3L)))
                .thenReturn(0);

        var product = productWithGallery(List.of(new FileResourceId(UUID7s.generate())));

        assertThrows(
                ProductVersionConflictException.class,
                () -> repository(productMapper, galleryMapper).update(product, 3L)
        );
        verify(galleryMapper, never()).deleteByProductId(any());
        verify(galleryMapper, never()).insert(any(ProductGalleryImagePO.class));
    }

    private static Product productWithGallery(List<FileResourceId> galleryImages) {
        return new Product(
                new ProductCode(UUID7s.generate()),
                ProductStatus.DRAFT,
                "Product",
                null,
                "Description",
                new CategoryCode("CATEGORY"),
                new FileResourceId(UUID7s.generate()),
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
                Mappers.getMapper(ProductPersistenceConverter.class)
        );
    }
}
