package org.ayachinene.app.domain.product;

public interface ProductRepository {

    void create(Product product);

    void update(Product product, long expectedVersion);

}
