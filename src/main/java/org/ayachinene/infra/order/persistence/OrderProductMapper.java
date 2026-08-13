package org.ayachinene.infra.order.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.ayachinene.shared.uuid7.UUID7;

import java.util.List;

@Mapper
public interface OrderProductMapper {

    OrderProductRow findBySkuCode(String skuCode);

    List<SpecificationSelectionRow> findSpecificationSelections(UUID7 skuId);
}
