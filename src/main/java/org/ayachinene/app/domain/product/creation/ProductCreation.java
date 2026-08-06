package org.ayachinene.app.domain.product.creation;

import org.ayachinene.app.domain.product.Product;
import org.ayachinene.app.domain.product.sku.Sku;
import org.ayachinene.app.domain.product.specification.Specification;

import java.util.List;
import java.util.Objects;

public record ProductCreation(
        Product product,
        List<Specification> specifications,
        List<Sku> skus
) {

    public ProductCreation {
        Objects.requireNonNull(product, "product must not be null");
        specifications = List.copyOf(
                Objects.requireNonNull(specifications, "specifications must not be null")
        );
        skus = List.copyOf(Objects.requireNonNull(skus, "skus must not be null"));
    }
}
