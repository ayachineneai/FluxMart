package org.ayachinene.app.order;

import org.ayachinene.api.order.data.CreateOrderRequest;
import org.ayachinene.app.order.creation.OrderValidator;
import org.ayachinene.app.order.repository.OrderProductRepository;
import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.product.domain.sku.SkuStatus;
import org.ayachinene.shared.exception.ValidationException;
import org.ayachinene.utils.Validates;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderProductRepository orderProductRepository;

    public OrderService(OrderProductRepository orderProductRepository) {
        this.orderProductRepository = orderProductRepository;
    }

    public void create(CreateOrderRequest request) {
        var validated = OrderValidator.validate(request);
        var product = orderProductRepository
            .findBySkuCode(new SkuCode(validated.skuCode()))
            .getOrElseThrow(() -> new ValidationException("sku does not exist"));
        Validates.require(
            isSellable(product.productStatus(), product.skuStatus()),
            "product is not sellable"
        );
        
    }

    private static boolean isSellable(
        ProductStatus productStatus,
        SkuStatus skuStatus
    ) {
        return productStatus == ProductStatus.ON_SALE
            && skuStatus == SkuStatus.ENABLED;
    }
}
