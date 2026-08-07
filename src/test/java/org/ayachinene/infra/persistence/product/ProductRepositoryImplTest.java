package org.ayachinene.infra.persistence.product;

import org.ayachinene.app.domain.product.CategoryCode;
import org.ayachinene.app.domain.product.Product;
import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.domain.product.ProductStatus;
import org.ayachinene.app.domain.product.ProductVersionConflictException;
import org.ayachinene.app.domain.product.creation.ProductCreation;
import org.ayachinene.app.domain.product.sku.SkuRepository;
import org.ayachinene.app.domain.product.specification.SpecificationRepository;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.shared.uuid7.UUID7s;
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
        var specificationRepository = mock(SpecificationRepository.class);
        var skuRepository = mock(SkuRepository.class);
        when(productMapper.insert(any(ProductPO.class))).thenReturn(1);
        when(galleryMapper.insert(any(ProductGalleryImagePO.class))).thenReturn(1);

        var firstImage = UUID7s.generate();
        var secondImage = UUID7s.generate();
        var product = new Product(
                ProductCode.generate(),
                ProductStatus.DRAFT,
                "Product",
                null,
                "Description",
                new CategoryCode("CATEGORY"),
                UUID7s.generate(),
                List.of(firstImage, secondImage)
        );

        new ProductRepositoryImpl(
                productMapper,
                galleryMapper,
                specificationRepository,
                skuRepository,
                Mappers.getMapper(ProductPersistenceConverter.class)
        ).create(new ProductCreation(product, List.of(), List.of()));

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
        verify(specificationRepository).create(product.productCode(), List.of());
        verify(skuRepository).create(product.productCode(), List.of());
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

        var galleryImage = UUID7s.generate();
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

        var product = productWithGallery(List.of(UUID7s.generate()));

        assertThrows(
                ProductVersionConflictException.class,
                () -> repository(productMapper, galleryMapper).update(product, 3L)
        );
        verify(galleryMapper, never()).deleteByProductId(any());
        verify(galleryMapper, never()).insert(any(ProductGalleryImagePO.class));
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
                mock(SpecificationRepository.class),
                mock(SkuRepository.class),
                Mappers.getMapper(ProductPersistenceConverter.class)
        );
    }
}
