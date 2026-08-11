package org.ayachinene.infra.product.persistence.specification;

import org.ayachinene.app.product.domain.specification.Specification;
import org.ayachinene.app.product.domain.specification.SpecificationStatus;
import org.ayachinene.app.product.domain.specification.SpecificationCode;
import org.ayachinene.app.product.domain.specification.SpecificationValue;
import org.ayachinene.app.product.domain.specification.SpecificationValueCode;
import org.ayachinene.shared.uuid7.UUID7s;
import org.ayachinene.infra.product.persistence.converter.SpecificationPersistenceConverter;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SpecificationWriterTest {

    @Test
    @SuppressWarnings("unchecked")
    void insertsSpecificationsAndValuesInDisplayOrder() {
        var specificationMapper = mock(SpecificationMapper.class);
        var valueMapper = mock(SpecificationValueMapper.class);
        var productId = UUID7s.generate();
        var createdAt = LocalDateTime.now();

        var black = value("黑色");
        var white = value("白色");
        var specification = new Specification(
                SpecificationCode.generate(),
                "颜色",
                SpecificationStatus.ENABLED,
                List.of(black, white)
        );

        var ids = new SpecificationWriter(
                specificationMapper,
                valueMapper,
                Mappers.getMapper(SpecificationPersistenceConverter.class)
        ).insert(productId, List.of(specification), createdAt);

        var specificationCaptor = ArgumentCaptor.forClass(List.class);
        verify(specificationMapper).insertBatch(specificationCaptor.capture());
        var savedSpecification = (SpecificationPO) specificationCaptor.getValue().getFirst();
        assertEquals(
                specification.specificationCode(),
                savedSpecification.getSpecificationCode()
        );
        assertEquals(
                ids.specificationId(specification.specificationCode()),
                savedSpecification.getId()
        );
        assertEquals(productId, savedSpecification.getProductId());
        assertEquals(0, savedSpecification.getSortOrder());
        assertEquals(createdAt, savedSpecification.getCreatedAt());

        var valueCaptor = ArgumentCaptor.forClass(List.class);
        verify(valueMapper).insertBatch(valueCaptor.capture());
        var savedValues = valueCaptor.getValue();
        var savedBlack = (SpecificationValuePO) savedValues.get(0);
        var savedWhite = (SpecificationValuePO) savedValues.get(1);
        assertEquals(black.specificationValueCode(), savedBlack.getSpecificationValueCode());
        assertEquals(
                ids.specificationValueId(black.specificationValueCode()),
                savedBlack.getId()
        );
        assertEquals(0, savedBlack.getSortOrder());
        assertEquals(white.specificationValueCode(), savedWhite.getSpecificationValueCode());
        assertEquals(
                ids.specificationValueId(white.specificationValueCode()),
                savedWhite.getId()
        );
        assertEquals(1, savedWhite.getSortOrder());
    }

    private static SpecificationValue value(String displayName) {
        return new SpecificationValue(
                SpecificationValueCode.generate(),
                displayName,
                SpecificationStatus.ENABLED
        );
    }
}
