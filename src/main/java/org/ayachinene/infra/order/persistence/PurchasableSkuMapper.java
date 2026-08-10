package org.ayachinene.infra.order.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.ayachinene.app.product.domain.sku.SkuCode;

import java.util.List;
import java.util.Set;

@Mapper
public interface PurchasableSkuMapper {

    List<PurchasableSkuRow> selectBySkuCodes(Set<SkuCode> skuCodes);
}
