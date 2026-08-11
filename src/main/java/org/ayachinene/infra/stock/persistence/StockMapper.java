package org.ayachinene.infra.stock.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.ayachinene.app.product.domain.sku.SkuCode;

import java.util.List;

@Mapper
public interface StockMapper {

    List<SkuCodeWithId> selectSkuCodesWithId(List<SkuCode> skuCodes);

    int insertBatch(List<StockPO> stocks);

    int reserve(ReserveStockCommand command);
}
