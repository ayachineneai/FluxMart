package org.ayachinene.infra.product.persistence.sku;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.product.domain.sku.Sku;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.product.domain.sku.SkuStatus;
import org.ayachinene.app.product.domain.sku.SpecificationSelection;
import org.ayachinene.app.product.domain.specification.SpecificationCode;
import org.ayachinene.app.product.domain.specification.SpecificationValueCode;
import org.ayachinene.infra.product.persistence.specification.SpecificationPersistenceIds;
import org.ayachinene.shared.uuid7.UUID7s;
import org.ayachinene.infra.product.persistence.converter.SkuPersistenceConverter;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

        var specificationCode = SpecificationCode.generate();
        var specificationValueCode = SpecificationValueCode.generate();
        var selection = new SpecificationSelection(
                specificationCode,
                specificationValueCode
        );
        var specificationId = UUID7s.generate();
        var specificationValueId = UUID7s.generate();
        var specificationIds = new SpecificationPersistenceIds(
                Map.of(specificationCode, specificationId),
                Map.of(specificationValueCode, specificationValueId)
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
        ).insert(productId, List.of(sku), specificationIds, createdAt);

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
        assertEquals(specificationId, savedSelection.getSpecificationId());
        assertEquals(specificationValueId, savedSelection.getSpecificationValueId());
    }
}
