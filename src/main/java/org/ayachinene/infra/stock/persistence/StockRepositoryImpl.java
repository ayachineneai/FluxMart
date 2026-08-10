package org.ayachinene.infra.stock.persistence;

import org.ayachinene.app.order.domain.OrderCode;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.stock.repository.StockRepository;
import org.ayachinene.app.stock.reservation.InsufficientStockException;
import org.ayachinene.app.stock.reservation.ReserveStockItem;
import org.ayachinene.app.stock.reservation.StockReservationStatus;
import org.ayachinene.shared.uuid7.UUID7s;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class StockRepositoryImpl implements StockRepository {

    private static final long INITIAL_QUANTITY = 0L;
    private static final long INITIAL_VERSION = 0L;

    private final StockMapper stockMapper;
    private final StockReservationMapper reservationMapper;

    public StockRepositoryImpl(
            StockMapper stockMapper,
            StockReservationMapper reservationMapper
    ) {
        this.stockMapper = stockMapper;
        this.reservationMapper = reservationMapper;
    }

    @Override
    public void initialize(List<SkuCode> skuCodes) {
        if (skuCodes.isEmpty()) {
            return;
        }
        var createdAt = LocalDateTime.now();
        var stockPos = stockMapper.selectSkuTargets(skuCodes).stream()
                .map(target -> new StockPO()
                        .setId(UUID7s.generate())
                        .setSkuId(target.getSkuId())
                        .setAvailableQuantity(INITIAL_QUANTITY)
                        .setReservedQuantity(INITIAL_QUANTITY)
                        .setVersion(INITIAL_VERSION)
                        .setCreatedAt(createdAt)
                        .setUpdatedAt(createdAt))
                .toList();
        if (stockPos.size() != skuCodes.size()) {
            throw new IllegalStateException("Not all SKU records were persisted");
        }
        stockMapper.insertBatch(stockPos);
    }

    @Override
    public void reserve(
            OrderCode orderCode,
            OffsetDateTime expiresAt,
            List<ReserveStockItem> items
    ) {
        var targetsBySkuCode = reservationMapper
                .selectTargetsByOrderCode(orderCode)
                .stream()
                .collect(Collectors.toMap(
                        StockReservationTarget::getSkuCode,
                        Function.identity()
                ));
        var reservedAt = LocalDateTime.now();

        items.forEach(item -> reserve(item, reservedAt));
        insertReservations(items, targetsBySkuCode, expiresAt, reservedAt);
    }

    private void reserve(ReserveStockItem item, LocalDateTime reservedAt) {
        var affectedRows = stockMapper.reserve(new ReserveStockCommand(
                item.skuCode(),
                item.quantity(),
                reservedAt
        ));
        if (affectedRows == 0) {
            throw new InsufficientStockException(item.skuCode());
        }
    }

    private void insertReservations(
            List<ReserveStockItem> items,
            java.util.Map<SkuCode, StockReservationTarget> targetsBySkuCode,
            OffsetDateTime expiresAt,
            LocalDateTime reservedAt
    ) {
        var reservationPos = items.stream()
                .map(item -> reservation(
                        target(item.skuCode(), targetsBySkuCode),
                        item.quantity(),
                        expiresAt,
                        reservedAt
                ))
                .toList();
        reservationMapper.insertBatch(reservationPos);
    }

    private StockReservationTarget target(
            SkuCode skuCode,
            java.util.Map<SkuCode, StockReservationTarget> targetsBySkuCode
    ) {
        var target = targetsBySkuCode.get(skuCode);
        if (target == null) {
            throw new IllegalStateException(
                    "Order item was not persisted for SKU: " + skuCode.value()
            );
        }
        return target;
    }

    private StockReservationPO reservation(
            StockReservationTarget target,
            int quantity,
            OffsetDateTime expiresAt,
            LocalDateTime reservedAt
    ) {
        return new StockReservationPO()
                .setId(UUID7s.generate())
                .setOrderId(target.getOrderId())
                .setOrderItemId(target.getOrderItemId())
                .setSkuId(target.getSkuId())
                .setQuantity((long) quantity)
                .setStatus(StockReservationStatus.RESERVED)
                .setExpiresAt(expiresAt.toLocalDateTime())
                .setCreatedAt(reservedAt)
                .setUpdatedAt(reservedAt);
    }
}
