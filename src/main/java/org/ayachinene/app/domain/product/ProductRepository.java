package org.ayachinene.app.domain.product;

import org.ayachinene.app.domain.product.creation.ProductCreation;

public interface ProductRepository {

    void create(ProductCreation creation);

    void update(Product product, long expectedVersion);

}
