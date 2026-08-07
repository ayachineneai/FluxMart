package org.ayachinene.infra.persistence.product.sku;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.domain.product.sku.Sku;
import org.ayachinene.app.domain.product.sku.SkuCode;
import org.ayachinene.app.domain.product.sku.SkuStatus;
import org.ayachinene.app.domain.product.sku.SpecificationSelection;
import org.ayachinene.shared.uuid7.UUID7s;
import org.ayachinene.infra.persistence.product.converter.SkuPersistenceConverter;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SkuWriterTest {

    @Test
    @SuppressWarnings("unchecked")
    void insertsSkuAndItsSpecificationSelections() {
        var skuMapper = mock(SkuMapper.class);
        var selectionMapper = mock(SkuSpecificationSelectionMapper.class);
        var productId = UUID7s.generate();
        var createdAt = LocalDateTime.now();

        var selection = new SpecificationSelection(
                UUID7s.generate(),
                UUID7s.generate()
        );
        var sku = new Sku(
                SkuCode.generate(),
                "SKU-BLACK",
                SkuStatus.DISABLED,
                new Money(new BigDecimal("99.00")),
                null,
                List.of(selection)
        );

        new SkuWriter(
                skuMapper,
                selectionMapper,
                Mappers.getMapper(SkuPersistenceConverter.class)
        ).insert(productId, List.of(sku), createdAt);

        var skuCaptor = ArgumentCaptor.forClass(List.class);
        verify(skuMapper).insertBatch(skuCaptor.capture());
        var savedSku = (SkuPO) skuCaptor.getValue().getFirst();
        assertNotNull(savedSku.getId());
        assertEquals(productId, savedSku.getProductId());
        assertEquals(sku.skuCode(), savedSku.getSkuCode());
        assertEquals(9900L, savedSku.getPriceAmount());
        assertEquals(0L, savedSku.getVersion());
        assertEquals(createdAt, savedSku.getCreatedAt());

        var selectionCaptor = ArgumentCaptor.forClass(List.class);
        verify(selectionMapper).insertBatch(selectionCaptor.capture());
        var savedSelection = (SkuSpecificationSelectionPO) selectionCaptor
                .getValue()
                .getFirst();
        assertEquals(savedSku.getId(), savedSelection.getSkuId());
        assertEquals(selection.specificationId(), savedSelection.getSpecificationId());
        assertEquals(
                selection.specificationValueId(),
                savedSelection.getSpecificationValueId()
        );
    }
}
