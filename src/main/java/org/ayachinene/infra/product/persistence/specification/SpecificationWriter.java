package org.ayachinene.infra.product.persistence.specification;

import org.ayachinene.app.product.domain.specification.Specification;
import org.ayachinene.infra.product.persistence.converter.SpecificationPersistenceConverter;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.utils.Streams;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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

    public void insert(
            UUID7 productId,
            List<Specification> specifications,
            LocalDateTime createdAt
    ) {
        if (specifications.isEmpty()) return;

        specificationMapper.insertBatch(
                toSpecificationPos(productId, specifications, createdAt)
        );
        specificationValueMapper.insertBatch(
                toSpecificationValuePos(specifications, createdAt)
        );
    }

    private List<SpecificationPO> toSpecificationPos(
            UUID7 productId,
            List<Specification> specifications,
            LocalDateTime createdAt
    ) {
        return Streams.withIndex(specifications)
                .map(indexed -> persistenceConverter
                        .toSpecificationPo(indexed.value())
                        .setProductId(productId)
                        .setSortOrder(indexed.index())
                        .setCreatedAt(createdAt)
                        .setUpdatedAt(createdAt)
                )
                .toList();
    }

    private List<SpecificationValuePO> toSpecificationValuePos(
            List<Specification> specifications,
            LocalDateTime createdAt
    ) {
        return specifications.stream()
                .flatMap(specification -> Streams.withIndex(specification.values())
                        .map(indexed -> persistenceConverter
                                .toSpecificationValuePo(indexed.value())
                                .setSpecificationId(specification.specificationId())
                                .setSortOrder(indexed.index())
                                .setCreatedAt(createdAt)
                                .setUpdatedAt(createdAt)
                        )
                )
                .toList();
    }
}
