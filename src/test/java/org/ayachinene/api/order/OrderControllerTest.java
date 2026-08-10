package org.ayachinene.api.order;

import org.ayachinene.api.order.controller.OrderController;
import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.order.creation.CreateOrderInput;
import org.ayachinene.app.order.creation.CreateOrderResult;
import org.ayachinene.app.order.creation.CreateOrderUseCase;
import org.ayachinene.app.order.domain.OrderCode;
import org.ayachinene.app.order.domain.OrderStatus;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.user.CurrentUserProvider;
import org.ayachinene.shared.uuid7.UUID7s;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class OrderControllerTest {

    @Test
    void createsOrderFromRequestHeaderAndCurrentUser() throws Exception {
        var createOrderUseCase = mock(CreateOrderUseCase.class);
        var currentUserProvider = mock(CurrentUserProvider.class);
        var userId = UUID7s.generate();
        var skuCode = SkuCode.generate();
        var orderCode = OrderCode.generate();
        var paymentExpiresAt = OffsetDateTime.parse(
                "2026-08-10T12:30:00+08:00"
        );
        when(currentUserProvider.currentUserId()).thenReturn(userId);
        when(createOrderUseCase.createOrder(any()))
                .thenReturn(new CreateOrderResult(
                        orderCode,
                        OrderStatus.PENDING_PAYMENT,
                        new Money(new BigDecimal("199.00")),
                        paymentExpiresAt
                ));
        var controller = new OrderController(
                createOrderUseCase,
                currentUserProvider,
                new OrderApiMapper()
        );

        standaloneSetup(controller)
                .build()
                .perform(post("/api/orders")
                        .header("Idempotency-Key", "request-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "items": [
                                    {
                                      "skuCode": "%s",
                                      "quantity": 2
                                    }
                                  ]
                                }
                                """.formatted(skuCode.value())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderCode").value(orderCode.value()))
                .andExpect(jsonPath("$.status").value("PENDING_PAYMENT"))
                .andExpect(jsonPath("$.totalAmount").value(199.00))
                .andExpect(jsonPath("$.paymentExpiresAt")
                        .value("2026-08-10T12:30:00+08:00"));

        var inputCaptor = ArgumentCaptor.forClass(CreateOrderInput.class);
        verify(createOrderUseCase).createOrder(inputCaptor.capture());
        var input = inputCaptor.getValue();
        assertEquals(userId, input.userId());
        assertEquals("request-001", input.requestKey());
        assertEquals(skuCode, input.items().getFirst().skuCode());
        assertEquals(2, input.items().getFirst().quantity());
    }
}
