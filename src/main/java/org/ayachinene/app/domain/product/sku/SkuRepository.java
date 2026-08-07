package org.ayachinene.app.domain.product.sku;

import org.ayachinene.app.domain.product.ProductCode;

public interface SkuRepository {

    boolean existsByProductCode(ProductCode productCode);
}
