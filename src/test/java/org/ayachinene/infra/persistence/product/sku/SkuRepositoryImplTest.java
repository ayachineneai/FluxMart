package org.ayachinene.infra.persistence.product.sku;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.domain.product.sku.Sku;
import org.ayachinene.app.domain.product.sku.SkuCode;
import org.ayachinene.app.domain.product.sku.SkuStatus;
import org.ayachinene.app.domain.product.sku.SpecificationSelection;
import org.ayachinene.app.domain.product.specification.SpecificationId;
import org.ayachinene.app.domain.product.specification.SpecificationValueId;
import org.ayachinene.app.uuid7.UUID7s;
import org.ayachinene.infra.persistence.product.ProductMapper;
import org.ayachinene.infra.persistence.product.converter.SkuPersistenceConverter;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SkuRepositoryImplTest {

    @Test
    void insertsSkuAndItsSpecificationSelections() {
        var productMapper = mock(ProductMapper.class);
        var skuMapper = mock(SkuMapper.class);
        var selectionMapper = mock(SkuSpecificationSelectionMapper.class);
        var productId = UUID7s.generate();
        var productCode = new ProductCode(UUID7s.generate());
        when(productMapper.selectIdByProductCode(productCode)).thenReturn(productId);

        var selection = new SpecificationSelection(
                new SpecificationId(UUID7s.generate()),
                new SpecificationValueId(UUID7s.generate())
        );
        var sku = new Sku(
                new SkuCode(UUID7s.generate()),
                "SKU-BLACK",
                SkuStatus.DISABLED,
                new Money(new BigDecimal("99.00")),
                null,
                List.of(selection)
        );

        new SkuRepositoryImpl(
                productMapper,
                skuMapper,
                selectionMapper,
                Mappers.getMapper(SkuPersistenceConverter.class)
        ).create(productCode, List.of(sku));

        var skuCaptor = ArgumentCaptor.forClass(SkuPO.class);
        verify(skuMapper).insert(skuCaptor.capture());
        var savedSku = skuCaptor.getValue();
        assertNotNull(savedSku.getId());
        assertEquals(productId, savedSku.getProductId());
        assertEquals(sku.skuCode(), savedSku.getSkuCode());
        assertEquals(9900L, savedSku.getPriceAmount());
        assertEquals(0L, savedSku.getVersion());

        var selectionCaptor = ArgumentCaptor.forClass(SkuSpecificationSelectionPO.class);
        verify(selectionMapper).insert(selectionCaptor.capture());
        var savedSelection = selectionCaptor.getValue();
        assertEquals(savedSku.getId(), savedSelection.getSkuId());
        assertEquals(selection.specificationId(), savedSelection.getSpecificationId());
        assertEquals(
                selection.specificationValueId(),
                savedSelection.getSpecificationValueId()
        );
    }
}
