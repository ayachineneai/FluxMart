package org.ayachinene.infra.order.persistence;

import io.vavr.control.Option;
import org.ayachinene.app.order.creation.OrderProductData;
import org.ayachinene.app.order.repository.OrderProductRepository;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.product.domain.specification.SpecificationCode;
import org.ayachinene.app.product.domain.specification.SpecificationValueCode;
import org.ayachinene.utils.Streams;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class OrderProductRepositoryImpl implements OrderProductRepository {

    private final OrderProductMapper mapper;

    public OrderProductRepositoryImpl(OrderProductMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Option<OrderProductData> findBySkuCode(SkuCode skuCode) {
        return Option.of(mapper.findBySkuCode(skuCode.value()))
            .map(this::toData);
    }

    private OrderProductData toData(OrderProductRow row) {
        var selections = Streams.of(mapper.findSpecificationSelections(row.getSkuId()))
            .map(this::toSelection)
            .toList();
        return new OrderProductData(
            row.getSkuId(),
            new SkuCode(row.getSkuCode()),
            row.getSkuStatus(),
            BigDecimal.valueOf(row.getPriceAmount(), 2),
            row.getStockId(),
            new ProductCode(row.getProductCode()),
            row.getProductStatus(),
            row.getProductTitle(),
            row.getSnapshotImageFileId(),
            selections
        );
    }

    private OrderProductData.SpecificationSelection toSelection(
        SpecificationSelectionRow row
    ) {
        return new OrderProductData.SpecificationSelection(
            new SpecificationCode(row.getSpecificationCode()),
            row.getSpecificationName(),
            new SpecificationValueCode(row.getSpecificationValueCode()),
            row.getSpecificationValue()
        );
    }
}
