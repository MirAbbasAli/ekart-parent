package com.ekart.orderservice.service;

import com.ekart.orderservice.dto.OrderLineItemsDto;
import com.ekart.orderservice.dto.OrderRequest;
import com.ekart.orderservice.entity.Order;
import com.ekart.orderservice.entity.OrderLineItems;
import com.ekart.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest){
        Order order=new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItems(orderLineItems);
        orderRepository.save(order);
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems=new OrderLineItems();
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
