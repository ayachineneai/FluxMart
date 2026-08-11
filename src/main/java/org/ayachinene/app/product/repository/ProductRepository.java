package org.ayachinene.app.product.repository;

import org.ayachinene.app.product.creation.CreateProductInput;
import org.ayachinene.app.product.creation.CreatedProduct;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.publication.ProductPublication;
import org.ayachinene.app.product.publication.ProductPublicationState;

public interface ProductRepository {

    CreatedProduct create(CreateProductInput input);

    ProductPublicationState queryPublicationState(ProductCode productCode);

    long publish(ProductPublication publication, long expectedVersion);

}
