package org.ayachinene.infra.persistence.product.sku;

import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.domain.product.sku.SkuRepository;
import org.springframework.stereotype.Repository;

@Repository
public class SkuRepositoryImpl implements SkuRepository {

    private final SkuMapper skuMapper;

    public SkuRepositoryImpl(SkuMapper skuMapper) {
        this.skuMapper = skuMapper;
    }

    @Override
    public boolean existsByProductCode(ProductCode productCode) {
        return skuMapper.existsByProductCode(productCode);
    }
}
