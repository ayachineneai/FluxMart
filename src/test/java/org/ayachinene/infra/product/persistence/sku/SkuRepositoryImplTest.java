package org.ayachinene.infra.product.persistence.sku;

import org.ayachinene.app.product.domain.ProductCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SkuRepositoryImplTest {

    @Test
    void checksWhetherAProductHasAnySku() {
        var skuMapper = mock(SkuMapper.class);
        var productCode = ProductCode.generate();
        var repository = new SkuRepositoryImpl(skuMapper);
        when(skuMapper.existsByProductCode(productCode)).thenReturn(true);

        assertTrue(repository.existsByProductCode(productCode));
        verify(skuMapper).existsByProductCode(productCode);
    }

    @Test
    void returnsFalseWhenTheProductHasNoSku() {
        var skuMapper = mock(SkuMapper.class);
        var productCode = ProductCode.generate();
        var repository = new SkuRepositoryImpl(skuMapper);
        when(skuMapper.existsByProductCode(productCode)).thenReturn(false);

        assertFalse(repository.existsByProductCode(productCode));
    }
}
