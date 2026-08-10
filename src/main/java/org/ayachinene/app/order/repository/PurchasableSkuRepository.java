package org.ayachinene.app.order.repository;

import org.ayachinene.app.order.query.PurchasableSku;
import org.ayachinene.app.product.domain.sku.SkuCode;

import java.util.List;
import java.util.Set;

public interface PurchasableSkuRepository {

    List<PurchasableSku> findBySkuCodes(Set<SkuCode> skuCodes);
}
