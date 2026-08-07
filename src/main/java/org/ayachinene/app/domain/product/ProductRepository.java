package org.ayachinene.app.domain.product;

import org.ayachinene.app.domain.product.creation.ProductCreation;
import org.ayachinene.app.domain.product.publication.ProductPublication;
import org.ayachinene.app.domain.product.publication.ProductPublicationState;

public interface ProductRepository {

    void create(ProductCreation creation);

    void update(Product product, long expectedVersion);

    ProductPublicationState findPublicationState(ProductCode productCode);

    long publish(ProductPublication publication, long expectedVersion);

}
