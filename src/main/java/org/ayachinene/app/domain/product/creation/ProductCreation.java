package org.ayachinene.app.domain.product.creation;

import org.ayachinene.app.domain.product.Product;
import org.ayachinene.app.domain.product.sku.Sku;
import org.ayachinene.app.domain.product.specification.Specification;

import java.util.List;

public record ProductCreation(
        Product product,
        List<Specification> specifications,
        List<Sku> skus
) {

    public ProductCreation {
        specifications = List.copyOf(specifications);
        skus = List.copyOf(skus);
    }
}
