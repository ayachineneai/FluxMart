package org.ayachinene.app.order.repository;

import io.vavr.control.Option;
import org.ayachinene.app.order.creation.OrderProductData;
import org.ayachinene.app.product.domain.sku.SkuCode;

public interface OrderProductRepository {

    Option<OrderProductData> findBySkuCode(SkuCode skuCode);
}
