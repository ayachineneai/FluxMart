package org.ayachinene.app.order;

import org.ayachinene.app.exception.ValidationException;
import org.ayachinene.app.order.creation.CreateOrderInput;
import org.ayachinene.app.order.creation.CreateOrderItemInput;
import org.ayachinene.app.order.creation.OrderValidator;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.shared.uuid7.UUID7s;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderValidatorTest {

    @Test
    void rejectsDuplicatedSkuAndInvalidQuantity() {
        var skuCode = SkuCode.generate();

        assertThrows(
                ValidationException.class,
                () -> OrderValidator.validate(new CreateOrderInput(
                        UUID7s.generate(),
                        "request-001",
                        List.of(
                                new CreateOrderItemInput(skuCode, 1),
                                new CreateOrderItemInput(skuCode, 2)
                        )
                ))
        );
        assertThrows(
                ValidationException.class,
                () -> OrderValidator.validate(new CreateOrderInput(
                        UUID7s.generate(),
                        "request-001",
                        List.of(new CreateOrderItemInput(skuCode, 0))
                ))
        );
    }
}
