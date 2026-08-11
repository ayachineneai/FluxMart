package org.ayachinene.infra.product.persistence;

import org.ayachinene.app.product.creation.CreateProductInput;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.ProductNotFoundException;
import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.app.product.domain.ProductVersionConflictException;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.product.publication.ProductPublication;
import org.ayachinene.infra.product.persistence.sku.SkuPO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductRepositoryImplTest {

    @Test
    void convertsAndInsertsProductInput() {
        var productMapper = mock(ProductMapper.class);
        var poFactory = mock(ProductCreationPOFactory.class);
        var inserter = mock(ProductCreationPersistenceInserter.class);
        var input = mock(CreateProductInput.class);
        var productCode = ProductCode.generate();
        var skuCode = SkuCode.generate();
        var pos = new ProductCreationPOs(
                new ProductPO().setProductCode(productCode),
                List.of(),
                List.of(),
                List.of(),
                List.of(new SkuPO().setSkuCode(skuCode)),
                List.of()
        );
        when(poFactory.toPos(input)).thenReturn(pos);

        var result = new ProductRepositoryImpl(
                productMapper,
                poFactory,
                inserter
        ).create(input);

        verify(poFactory).toPos(input);
        verify(inserter).insert(pos);
        assertEquals(productCode, result.productCode());
        assertEquals(List.of(skuCode), result.skuCodes());
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

        var version = repository(productMapper).publish(
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
                () -> repository(productMapper).publish(
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

        var state = repository(productMapper).queryPublicationState(productCode);

        assertEquals(productCode, state.productCode());
        assertEquals(ProductStatus.OFF_SALE, state.status());
        assertEquals(3L, state.version());
    }

    @Test
    void rejectsFindingPublicationStateForUnknownProduct() {
        var productCode = ProductCode.generate();

        assertThrows(
                ProductNotFoundException.class,
                () -> repository(mock(ProductMapper.class))
                        .queryPublicationState(productCode)
        );
    }

    private static ProductRepositoryImpl repository(ProductMapper productMapper) {
        return new ProductRepositoryImpl(
                productMapper,
                mock(ProductCreationPOFactory.class),
                mock(ProductCreationPersistenceInserter.class)
        );
    }
}
