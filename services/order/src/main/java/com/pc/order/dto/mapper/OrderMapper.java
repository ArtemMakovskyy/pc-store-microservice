package com.pc.order.dto.mapper;

import com.pc.order.dto.OrderRequest;
import com.pc.order.dto.OrderResponse;
import com.pc.order.model.Order;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class OrderMapper {

    public Order toOrder(OrderRequest request) {
        if (request == null) {
            return null;
        }
        return Order.builder()
                .id(request.id())
                .reference(UUID.randomUUID().toString())
                .paymentMethod(request.paymentMethod())
                .customerId(request.customerId())
                .build();
    }

    public OrderResponse fromOrder(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getReference(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                order.getCustomerId()
        );
    }
}
