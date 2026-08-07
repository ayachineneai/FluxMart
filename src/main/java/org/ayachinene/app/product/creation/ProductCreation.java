package org.ayachinene.app.product.creation;

import org.ayachinene.app.product.domain.Product;
import org.ayachinene.app.product.domain.sku.Sku;
import org.ayachinene.app.product.domain.specification.Specification;

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
