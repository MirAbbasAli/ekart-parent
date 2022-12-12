package com.ekart.orderservice.service;

import brave.Span;
import brave.Tracer;
import com.ekart.orderservice.dto.InventoryResponse;
import com.ekart.orderservice.dto.OrderLineItemsDto;
import com.ekart.orderservice.dto.OrderRequest;
import com.ekart.orderservice.entity.Order;
import com.ekart.orderservice.entity.OrderLineItems;
import com.ekart.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;

    public String placeOrder(OrderRequest orderRequest){
        Order order=new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItems(orderLineItems);

        List<String> skuCodes = order.getOrderLineItems().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        // Creating own span
        Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");
        // try with resources
        try(Tracer.SpanInScope isLookup=tracer.withSpanInScope(inventoryServiceLookup.start())){
            inventoryServiceLookup.tag("call", "inventory-service");
            // Call Inventory Service, and place order if product is in stock
            InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                    .uri("http://inventory-service/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();
            Boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::getIsInStock);
            if(allProductsInStock) {
                orderRepository.save(order);
                return "Order Placed Successfully";
            }else {
                throw new IllegalArgumentException("Product not in stock, please try again later");
            }
        } finally{
            inventoryServiceLookup.flush();
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems=new OrderLineItems();
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
