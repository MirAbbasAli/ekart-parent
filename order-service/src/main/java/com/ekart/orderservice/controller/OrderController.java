package com.ekart.orderservice.controller;

import com.ekart.orderservice.dto.OrderRequest;
import com.ekart.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory", fallbackMethod = "placeOrderFallback")
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        orderService.placeOrder(orderRequest);
        return "Order Placed Successfully";
    }

    public String placeOrderFallback(OrderRequest orderRequest, RuntimeException runtimeException){
        return "Oops! Something went wrong, please order after some time!";
    }
}
