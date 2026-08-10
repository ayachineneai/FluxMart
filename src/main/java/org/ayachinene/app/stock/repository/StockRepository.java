package org.ayachinene.app.stock.repository;

import org.ayachinene.app.order.domain.OrderCode;
import org.ayachinene.app.stock.reservation.ReserveStockItem;
import org.ayachinene.app.product.domain.sku.SkuCode;

import java.time.OffsetDateTime;
import java.util.List;

public interface StockRepository {

    void initialize(List<SkuCode> skuCodes);

    void reserve(
            OrderCode orderCode,
            OffsetDateTime expiresAt,
            List<ReserveStockItem> items
    );
}
