package org.ayachinene.app.product.repository;

import org.ayachinene.app.product.domain.ProductCode;

public interface SkuRepository {

    boolean existsByProductCode(ProductCode productCode);
}
