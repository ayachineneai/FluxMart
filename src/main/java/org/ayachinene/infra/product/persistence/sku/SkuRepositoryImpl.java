package org.ayachinene.infra.product.persistence.sku;

import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.repository.SkuRepository;
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
