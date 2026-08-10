package org.ayachinene.api.order.controller;

import org.ayachinene.api.order.OrderApiMapper;
import org.ayachinene.api.order.data.CreateOrderRequest;
import org.ayachinene.api.order.data.CreateOrderResponse;
import org.ayachinene.app.order.creation.CreateOrderUseCase;
import org.ayachinene.app.user.CurrentUserProvider;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final CurrentUserProvider currentUserProvider;
    private final OrderApiMapper orderMapper;

    public OrderController(
            CreateOrderUseCase createOrderUseCase,
            CurrentUserProvider currentUserProvider,
            OrderApiMapper orderMapper
    ) {
        this.createOrderUseCase = createOrderUseCase;
        this.currentUserProvider = currentUserProvider;
        this.orderMapper = orderMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderResponse createOrder(
            @RequestHeader("Idempotency-Key") String requestKey,
            @RequestBody CreateOrderRequest request
    ) {
        var input = orderMapper.toInput(
                currentUserProvider.currentUserId(),
                requestKey,
                request
        );
        var result = createOrderUseCase.createOrder(input);
        return orderMapper.toResponse(result);
    }
}
