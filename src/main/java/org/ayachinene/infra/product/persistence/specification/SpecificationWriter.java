package org.ayachinene.infra.product.persistence.specification;

import org.ayachinene.app.product.domain.specification.Specification;
import org.ayachinene.app.product.domain.specification.SpecificationCode;
import org.ayachinene.app.product.domain.specification.SpecificationValueCode;
import org.ayachinene.infra.product.persistence.converter.SpecificationPersistenceConverter;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.shared.uuid7.UUID7s;
import org.ayachinene.utils.Streams;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.LinkedHashMap;

@Repository
public class SpecificationWriter {

    private final SpecificationMapper specificationMapper;
    private final SpecificationValueMapper specificationValueMapper;
    private final SpecificationPersistenceConverter persistenceConverter;

    public SpecificationWriter(
            SpecificationMapper specificationMapper,
            SpecificationValueMapper specificationValueMapper,
            SpecificationPersistenceConverter persistenceConverter
    ) {
        this.specificationMapper = specificationMapper;
        this.specificationValueMapper = specificationValueMapper;
        this.persistenceConverter = persistenceConverter;
    }

    public SpecificationPersistenceIds insert(
            UUID7 productId,
            List<Specification> specifications,
            LocalDateTime createdAt
    ) {
        var ids = assignIds(specifications);
        if (specifications.isEmpty()) return ids;

        specificationMapper.insertBatch(
                toSpecificationPos(productId, specifications, ids, createdAt)
        );
        specificationValueMapper.insertBatch(
                toSpecificationValuePos(specifications, ids, createdAt)
        );
        return ids;
    }

    private SpecificationPersistenceIds assignIds(List<Specification> specifications) {
        var specificationIds = new LinkedHashMap<SpecificationCode, UUID7>();
        var valueIds = new LinkedHashMap<SpecificationValueCode, UUID7>();
        specifications.forEach(specification -> {
            specificationIds.put(specification.specificationCode(), UUID7s.generate());
            specification.values().forEach(value ->
                    valueIds.put(value.specificationValueCode(), UUID7s.generate())
            );
        });
        return new SpecificationPersistenceIds(specificationIds, valueIds);
    }

    private List<SpecificationPO> toSpecificationPos(
            UUID7 productId,
            List<Specification> specifications,
            SpecificationPersistenceIds ids,
            LocalDateTime createdAt
    ) {
        return Streams.withIndex(specifications)
                .map(indexed -> persistenceConverter
                        .toSpecificationPo(indexed.value())
                        .setId(ids.specificationId(indexed.value().specificationCode()))
                        .setProductId(productId)
                        .setSortOrder(indexed.index())
                        .setCreatedAt(createdAt)
                        .setUpdatedAt(createdAt)
                )
                .toList();
    }

    private List<SpecificationValuePO> toSpecificationValuePos(
            List<Specification> specifications,
            SpecificationPersistenceIds ids,
            LocalDateTime createdAt
    ) {
        return specifications.stream()
                .flatMap(specification -> Streams.withIndex(specification.values())
                        .map(indexed -> persistenceConverter
                                .toSpecificationValuePo(indexed.value())
                                .setId(ids.specificationValueId(
                                        indexed.value().specificationValueCode()
                                ))
                                .setSpecificationId(ids.specificationId(
                                        specification.specificationCode()
                                ))
                                .setSortOrder(indexed.index())
                                .setCreatedAt(createdAt)
                                .setUpdatedAt(createdAt)
                        )
                )
                .toList();
    }
}
