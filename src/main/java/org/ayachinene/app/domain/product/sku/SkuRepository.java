package org.ayachinene.app.domain.product.sku;

import org.ayachinene.app.domain.product.ProductCode;

import java.util.List;

public interface SkuRepository {

    void create(ProductCode productCode, List<Sku> skus);
}
