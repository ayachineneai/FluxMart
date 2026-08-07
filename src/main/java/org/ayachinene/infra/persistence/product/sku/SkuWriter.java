package org.ayachinene.infra.persistence.product.sku;

import org.ayachinene.app.domain.product.sku.Sku;
import org.ayachinene.infra.persistence.product.converter.SkuPersistenceConverter;
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
            LocalDateTime createdAt
    ) {
        if (skus.isEmpty()) return;

        var identifiedSkus = skus.stream()
                .map(sku -> new IdentifiedSku(UUID7s.generate(), sku))
                .toList();
        var selectionPos = toSelectionPos(identifiedSkus, createdAt);

        skuMapper.insertBatch(toSkuPos(productId, identifiedSkus, createdAt));
        if (!selectionPos.isEmpty()) {
            selectionMapper.insertBatch(selectionPos);
        }
    }

    private List<SkuPO> toSkuPos(
            UUID7 productId,
            List<IdentifiedSku> skus,
            LocalDateTime createdAt
    ) {
        return skus.stream()
                .map(identified -> persistenceConverter.toSkuPo(identified.sku())
                        .setId(identified.id())
                        .setProductId(productId)
                        .setVersion(INITIAL_VERSION)
                        .setCreatedAt(createdAt)
                        .setUpdatedAt(createdAt)
                )
                .toList();
    }

    private List<SkuSpecificationSelectionPO> toSelectionPos(
            List<IdentifiedSku> skus,
            LocalDateTime createdAt
    ) {
        return skus.stream()
                .flatMap(identified -> identified.sku().specificationSelections().stream()
                        .map(selection -> persistenceConverter.toSelectionPo(selection)
                                .setId(UUID7s.generate())
                                .setSkuId(identified.id())
                                .setCreatedAt(createdAt)
                        )
                )
                .toList();
    }

    private record IdentifiedSku(UUID7 id, Sku sku) {
    }
}
