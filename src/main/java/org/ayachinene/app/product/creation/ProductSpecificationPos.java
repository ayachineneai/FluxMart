package org.ayachinene.app.product.creation;

import org.ayachinene.infra.product.persistence.ProductSpecificationPO;
import org.ayachinene.infra.product.persistence.ProductSpecificationValuePO;

import java.util.List;

public record ProductSpecificationPos(
    ProductSpecificationPO specification,
    List<ProductSpecificationValuePO> values
) {
}
