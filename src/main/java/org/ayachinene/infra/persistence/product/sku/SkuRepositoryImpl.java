package org.ayachinene.infra.persistence.product.sku;

import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.domain.product.ProductNotFoundException;
import org.ayachinene.app.domain.product.sku.Sku;
import org.ayachinene.app.domain.product.sku.SkuRepository;
import org.ayachinene.app.uuid7.UUID7;
import org.ayachinene.app.uuid7.UUID7s;
import org.ayachinene.infra.persistence.product.ProductMapper;
import org.ayachinene.infra.persistence.product.converter.SkuPersistenceConverter;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class SkuRepositoryImpl implements SkuRepository {

    private static final long INITIAL_VERSION = 0L;

    private final ProductMapper productMapper;
    private final SkuMapper skuMapper;
    private final SkuSpecificationSelectionMapper selectionMapper;
    private final SkuPersistenceConverter persistenceConverter;

    public SkuRepositoryImpl(
            ProductMapper productMapper,
            SkuMapper skuMapper,
            SkuSpecificationSelectionMapper selectionMapper,
            SkuPersistenceConverter persistenceConverter
    ) {
        this.productMapper = productMapper;
        this.skuMapper = skuMapper;
        this.selectionMapper = selectionMapper;
        this.persistenceConverter = persistenceConverter;
    }

    @Override
    public void create(ProductCode productCode, List<Sku> skus) {
        if (skus.isEmpty()) {
            return;
        }
        var productId = productMapper.selectIdByProductCode(productCode);
        if (productId == null) {
            throw new ProductNotFoundException(productCode);
        }
        var createdAt = LocalDateTime.now();
        skus.forEach(sku -> insertSku(sku, productId, createdAt));
    }

    private void insertSku(Sku sku, UUID7 productId, LocalDateTime createdAt) {
        var newSkuId = UUID7s.generate();
        var skuPo = persistenceConverter.toSkuPo(sku)
                .setId(newSkuId)
                .setProductId(productId)
                .setVersion(INITIAL_VERSION)
                .setCreatedAt(createdAt)
                .setUpdatedAt(createdAt);
        skuMapper.insert(skuPo);

        sku.specificationSelections().forEach(selection -> {
            var selectionPo = persistenceConverter.toSelectionPo(selection)
                    .setId(UUID7s.generate())
                    .setSkuId(newSkuId)
                    .setCreatedAt(createdAt);
            selectionMapper.insert(selectionPo);
        });
    }
}
