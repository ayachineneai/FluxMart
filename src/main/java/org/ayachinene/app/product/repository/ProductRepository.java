package org.ayachinene.app.product.repository;

import org.ayachinene.app.product.domain.Product;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.creation.ProductCreation;
import org.ayachinene.app.product.publication.ProductPublication;
import org.ayachinene.app.product.publication.ProductPublicationState;

public interface ProductRepository {

    void create(ProductCreation creation);

    void update(Product product, long expectedVersion);

    ProductPublicationState queryPublicationState(ProductCode productCode);

    long publish(ProductPublication publication, long expectedVersion);

}
