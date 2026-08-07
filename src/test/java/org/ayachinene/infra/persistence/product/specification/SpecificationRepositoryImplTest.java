package org.ayachinene.infra.persistence.product.specification;

import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.domain.product.specification.Specification;
import org.ayachinene.app.domain.product.specification.SpecificationStatus;
import org.ayachinene.app.domain.product.specification.SpecificationValue;
import org.ayachinene.shared.uuid7.UUID7s;
import org.ayachinene.infra.persistence.product.ProductMapper;
import org.ayachinene.infra.persistence.product.converter.SpecificationPersistenceConverter;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SpecificationRepositoryImplTest {

    @Test
    void insertsSpecificationsAndValuesInDisplayOrder() {
        var productMapper = mock(ProductMapper.class);
        var specificationMapper = mock(SpecificationMapper.class);
        var valueMapper = mock(SpecificationValueMapper.class);
        var productId = UUID7s.generate();
        var productCode = ProductCode.generate();
        when(productMapper.selectIdByProductCode(productCode)).thenReturn(productId);

        var black = value("黑色");
        var white = value("白色");
        var specification = new Specification(
                UUID7s.generate(),
                "颜色",
                SpecificationStatus.ENABLED,
                List.of(black, white)
        );

        new SpecificationRepositoryImpl(
                productMapper,
                specificationMapper,
                valueMapper,
                Mappers.getMapper(SpecificationPersistenceConverter.class)
        ).create(productCode, List.of(specification));

        var specificationCaptor = ArgumentCaptor.forClass(SpecificationPO.class);
        verify(specificationMapper).insert(specificationCaptor.capture());
        assertEquals(specification.specificationId(), specificationCaptor.getValue().getId());
        assertEquals(productId, specificationCaptor.getValue().getProductId());
        assertEquals(0, specificationCaptor.getValue().getSortOrder());

        var valueCaptor = ArgumentCaptor.forClass(SpecificationValuePO.class);
        verify(valueMapper, times(2)).insert(valueCaptor.capture());
        assertEquals(black.specificationValueId(), valueCaptor.getAllValues().get(0).getId());
        assertEquals(0, valueCaptor.getAllValues().get(0).getSortOrder());
        assertEquals(white.specificationValueId(), valueCaptor.getAllValues().get(1).getId());
        assertEquals(1, valueCaptor.getAllValues().get(1).getSortOrder());
    }

    private static SpecificationValue value(String displayName) {
        return new SpecificationValue(
                UUID7s.generate(),
                displayName,
                SpecificationStatus.ENABLED
        );
    }
}
