package org.ayachinene.infra.product.persistence.sku;

import org.ayachinene.app.product.domain.sku.Sku;
import org.ayachinene.infra.product.persistence.converter.SkuPersistenceConverter;
import org.ayachinene.infra.product.persistence.specification.SpecificationPersistenceIds;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.shared.uuid7.UUID7s;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class SkuWriter {

    private static final long INITIAL_VERSION = 0L;

    private final SkuMapper skuMapper;
    private final SkuSpecificationSelectionMapper selectionMapper;
    private final SkuPersistenceConverter persistenceConverter;

    public SkuWriter(
            SkuMapper skuMapper,
            SkuSpecificationSelectionMapper selectionMapper,
            SkuPersistenceConverter persistenceConverter
    ) {
        this.skuMapper = skuMapper;
        this.selectionMapper = selectionMapper;
        this.persistenceConverter = persistenceConverter;
    }

    public void insert(
            UUID7 productId,
            List<Sku> skus,
            SpecificationPersistenceIds specificationIds,
            LocalDateTime createdAt
    ) {
        if (skus.isEmpty()) return;

        var skuItems = assignIds(skus);
        var selectionPos = toSelectionPos(skuItems, specificationIds, createdAt);

        skuMapper.insertBatch(toSkuPos(productId, skuItems, createdAt));
        if (!selectionPos.isEmpty()) {
            selectionMapper.insertBatch(selectionPos);
        }
    }

    private List<SkuWithId> assignIds(List<Sku> skus) {
        return skus.stream()
                .map(sku -> new SkuWithId(UUID7s.generate(), sku))
                .toList();
    }

    private List<SkuSpecificationSelectionPO> toSelectionPos(
        List<SkuWithId> skus,
        SpecificationPersistenceIds specificationIds,
        LocalDateTime createdAt
    ) {
        return skus.stream()
            .flatMap(item -> item.sku().specificationSelections().stream()
                .map(selection -> persistenceConverter.toSelectionPo(selection)
                    .setId(UUID7s.generate())
                    .setSkuId(item.skuId())
                    .setSpecificationId(specificationIds.specificationId(
                            selection.specificationCode()
                    ))
                    .setSpecificationValueId(specificationIds.specificationValueId(
                            selection.specificationValueCode()
                    ))
                    .setCreatedAt(createdAt)
                )
            )
            .toList();
    }

    private List<SkuPO> toSkuPos(
            UUID7 productId,
            List<SkuWithId> skus,
            LocalDateTime createdAt
    ) {
        return skus.stream()
                .map(item -> persistenceConverter.toSkuPo(item.sku())
                        .setId(item.skuId())
                        .setProductId(productId)
                        .setVersion(INITIAL_VERSION)
                        .setCreatedAt(createdAt)
                        .setUpdatedAt(createdAt)
                )
                .toList();
    }

    private record SkuWithId(UUID7 skuId, Sku sku) {
    }
}
