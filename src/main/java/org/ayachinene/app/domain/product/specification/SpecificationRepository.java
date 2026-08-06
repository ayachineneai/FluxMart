package org.ayachinene.app.domain.product.specification;

import org.ayachinene.app.domain.product.ProductCode;

import java.util.List;

public interface SpecificationRepository {

    void create(ProductCode productCode, List<Specification> specifications);
}
