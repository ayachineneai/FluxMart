package org.ayachinene.infra.order.persistence;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.order.domain.Order;
import org.ayachinene.app.order.domain.OrderItem;
import org.ayachinene.app.order.domain.SpecificationSnapshotItem;
import org.ayachinene.app.product.domain.specification.SpecificationCode;
import org.ayachinene.app.product.domain.specification.SpecificationValueCode;
import org.ayachinene.infra.exception.UncheckedException;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.List;

@Component
public class OrderPersistenceConverter {

    private static final BigDecimal MINOR_UNIT = BigDecimal.valueOf(100);

    private final ObjectMapper objectMapper;

    public OrderPersistenceConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public OrderPO toOrderPo(Order order) {
        return new OrderPO()
                .setOrderCode(order.orderCode())
                .setUserId(order.userId())
                .setRequestKey(order.requestKey())
                .setStatus(order.status())
                .setTotalAmount(toMinorAmount(order.totalAmount()))
                .setPaymentExpiresAt(order.paymentExpiresAt().toLocalDateTime());
    }

    public OrderItemPO toOrderItemPo(OrderItem item) {
        return new OrderItemPO()
                .setProductCode(item.productCode())
                .setSkuCode(item.skuCode())
                .setProductTitle(item.productTitle())
                .setSpecificationSnapshot(writeSnapshot(item.specificationSnapshot()))
                .setImageFileId(item.imageFileId())
                .setUnitPriceAmount(toMinorAmount(item.unitPrice()))
                .setQuantity(item.quantity())
                .setTotalAmount(toMinorAmount(item.totalAmount()));
    }

    public Order toOrder(OrderPO order, List<OrderItemPO> items) {
        return new Order(
                order.getOrderCode(),
                order.getUserId(),
                order.getRequestKey(),
                order.getStatus(),
                fromMinorAmount(order.getTotalAmount()),
                order.getPaymentExpiresAt()
                        .atZone(ZoneId.systemDefault())
                        .toOffsetDateTime(),
                items.stream().map(this::toOrderItem).toList()
        );
    }

    private OrderItem toOrderItem(OrderItemPO item) {
        return new OrderItem(
                item.getProductCode(),
                item.getSkuCode(),
                item.getProductTitle(),
                readSnapshot(item.getSpecificationSnapshot()),
                item.getImageFileId(),
                fromMinorAmount(item.getUnitPriceAmount()),
                item.getQuantity(),
                fromMinorAmount(item.getTotalAmount())
        );
    }

    private long toMinorAmount(Money money) {
        return money.amount().multiply(MINOR_UNIT).longValueExact();
    }

    private Money fromMinorAmount(long amount) {
        return new Money(BigDecimal.valueOf(amount, 2));
    }

    private String writeSnapshot(List<SpecificationSnapshotItem> snapshot) {
        var jsonItems = snapshot.stream()
                .map(SnapshotJson::from)
                .toList();
        try {
            return objectMapper.writeValueAsString(jsonItems);
        } catch (JacksonException exception) {
            throw new UncheckedException(exception);
        }
    }

    private List<SpecificationSnapshotItem> readSnapshot(String value) {
        try {
            return objectMapper.readValue(
                    value,
                    new TypeReference<List<SnapshotJson>>() {
                    }
            ).stream().map(SnapshotJson::toSnapshot).toList();
        } catch (JacksonException exception) {
            throw new UncheckedException(exception);
        }
    }

    private record SnapshotJson(
            String specificationCode,
            String specificationName,
            String specificationValueCode,
            String specificationValueName
    ) {

        private static SnapshotJson from(SpecificationSnapshotItem item) {
            return new SnapshotJson(
                    item.specificationCode().value(),
                    item.specificationName(),
                    item.specificationValueCode().value(),
                    item.specificationValueName()
            );
        }

        private SpecificationSnapshotItem toSnapshot() {
            return new SpecificationSnapshotItem(
                    new SpecificationCode(specificationCode),
                    specificationName,
                    new SpecificationValueCode(specificationValueCode),
                    specificationValueName
            );
        }
    }
}
