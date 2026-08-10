package org.ayachinene.infra.stock.persistence;

import org.ayachinene.app.order.domain.OrderCode;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.stock.reservation.InsufficientStockException;
import org.ayachinene.app.stock.reservation.ReserveStockItem;
import org.ayachinene.app.stock.reservation.StockReservationStatus;
import org.ayachinene.shared.uuid7.UUID7s;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StockRepositoryImplTest {

    @Test
    void initializesZeroStockForEverySku() {
        var stockMapper = mock(StockMapper.class);
        var reservationMapper = mock(StockReservationMapper.class);
        var skuCode = SkuCode.generate();
        var target = new SkuStockTarget();
        target.setSkuId(UUID7s.generate());
        target.setSkuCode(skuCode);
        when(stockMapper.selectSkuTargets(List.of(skuCode)))
                .thenReturn(List.of(target));
        var repository = new StockRepositoryImpl(stockMapper, reservationMapper);

        repository.initialize(List.of(skuCode));

        var stocks = stockBatch(stockMapper);
        assertEquals(1, stocks.size());
        assertEquals(target.getSkuId(), stocks.getFirst().getSkuId());
        assertEquals(0L, stocks.getFirst().getAvailableQuantity());
        assertEquals(0L, stocks.getFirst().getReservedQuantity());
    }

    @Test
    void reservesStockAndCreatesReservation() {
        var stockMapper = mock(StockMapper.class);
        var reservationMapper = mock(StockReservationMapper.class);
        var skuCode = SkuCode.generate();
        var target = reservationTarget(skuCode);
        var orderCode = OrderCode.generate();
        when(reservationMapper.selectTargetsByOrderCode(orderCode))
                .thenReturn(List.of(target));
        when(stockMapper.reserve(any())).thenReturn(1);
        var repository = new StockRepositoryImpl(stockMapper, reservationMapper);

        repository.reserve(
                orderCode,
                OffsetDateTime.now().plusMinutes(15),
                List.of(new ReserveStockItem(skuCode, 2))
        );

        var reservations = reservationBatch(reservationMapper);
        assertEquals(1, reservations.size());
        assertEquals(target.getOrderItemId(), reservations.getFirst().getOrderItemId());
        assertEquals(2L, reservations.getFirst().getQuantity());
        assertEquals(StockReservationStatus.RESERVED,
                reservations.getFirst().getStatus());
    }

    @Test
    void rejectsInsufficientStock() {
        var stockMapper = mock(StockMapper.class);
        var reservationMapper = mock(StockReservationMapper.class);
        var skuCode = SkuCode.generate();
        var orderCode = OrderCode.generate();
        when(reservationMapper.selectTargetsByOrderCode(orderCode))
                .thenReturn(List.of(reservationTarget(skuCode)));
        when(stockMapper.reserve(any())).thenReturn(0);
        var repository = new StockRepositoryImpl(stockMapper, reservationMapper);

        assertThrows(
                InsufficientStockException.class,
                () -> repository.reserve(
                        orderCode,
                        OffsetDateTime.now().plusMinutes(15),
                        List.of(new ReserveStockItem(skuCode, 2))
                )
        );
    }

    private StockReservationTarget reservationTarget(SkuCode skuCode) {
        var target = new StockReservationTarget();
        target.setOrderId(UUID7s.generate());
        target.setOrderItemId(UUID7s.generate());
        target.setSkuId(UUID7s.generate());
        target.setSkuCode(skuCode);
        return target;
    }

    @SuppressWarnings("unchecked")
    private List<StockPO> stockBatch(StockMapper mapper) {
        var captor = ArgumentCaptor.forClass(List.class);
        verify(mapper).insertBatch(captor.capture());
        return captor.getValue();
    }

    @SuppressWarnings("unchecked")
    private List<StockReservationPO> reservationBatch(
            StockReservationMapper mapper
    ) {
        var captor = ArgumentCaptor.forClass(List.class);
        verify(mapper).insertBatch(captor.capture());
        return captor.getValue();
    }
}
