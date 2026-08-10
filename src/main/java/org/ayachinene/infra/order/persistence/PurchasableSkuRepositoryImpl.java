package org.ayachinene.infra.order.persistence;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.order.domain.SpecificationSnapshotItem;
import org.ayachinene.app.order.query.PurchasableSku;
import org.ayachinene.app.order.repository.PurchasableSkuRepository;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Repository
public class PurchasableSkuRepositoryImpl implements PurchasableSkuRepository {

    private final PurchasableSkuMapper skuMapper;

    public PurchasableSkuRepositoryImpl(PurchasableSkuMapper skuMapper) {
        this.skuMapper = skuMapper;
    }

    @Override
    public List<PurchasableSku> findBySkuCodes(Set<SkuCode> skuCodes) {
        if (skuCodes.isEmpty()) {
            return List.of();
        }
        var rowsBySkuCode = new LinkedHashMap<SkuCode, List<PurchasableSkuRow>>();
        skuMapper.selectBySkuCodes(skuCodes).forEach(row ->
                rowsBySkuCode.computeIfAbsent(
                        row.getSkuCode(),
                        ignored -> new ArrayList<>()
                ).add(row)
        );
        return rowsBySkuCode.values().stream()
                .map(this::toPurchasableSku)
                .toList();
    }

    private PurchasableSku toPurchasableSku(List<PurchasableSkuRow> rows) {
        var sku = rows.getFirst();
        return new PurchasableSku(
                sku.getSkuId(),
                sku.getProductCode(),
                sku.getSkuCode(),
                sku.getProductStatus(),
                sku.getSkuStatus(),
                sku.getProductTitle(),
                specificationSnapshot(rows),
                sku.getImageFileId(),
                new Money(BigDecimal.valueOf(sku.getPriceAmount(), 2))
        );
    }

    private List<SpecificationSnapshotItem> specificationSnapshot(
            List<PurchasableSkuRow> rows
    ) {
        return rows.stream()
                .filter(row -> row.getSpecificationId() != null)
                .map(row -> new SpecificationSnapshotItem(
                        row.getSpecificationId(),
                        row.getSpecificationName(),
                        row.getSpecificationValueId(),
                        row.getSpecificationValueName()
                ))
                .toList();
    }
}
