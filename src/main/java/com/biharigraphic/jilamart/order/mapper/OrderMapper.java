package com.biharigraphic.jilamart.order.mapper;

import com.biharigraphic.jilamart.entity.OrderItem;
import com.biharigraphic.jilamart.order.dto.OrderItemResponse;
import com.biharigraphic.jilamart.order.dto.OrderResponse;
import com.biharigraphic.jilamart.order.entity.OrderEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderResponse toResponse(OrderEntity order) {

        OrderResponse res = new OrderResponse();

        res.setOrderId(order.getId());
        res.setStatus(order.getOrderStatus().name());
        res.setTotalAmount(order.getTotalAmount());
        res.setFinalAmount(order.getFinalAmount());

        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::mapItem)
                .toList();

        res.setItems(items);

        return res;
    }

    private OrderItemResponse mapItem(OrderItem item) {

        OrderItemResponse res = new OrderItemResponse();

        res.setProductName(item.getProduct().getName());
        res.setQuantity(item.getQuantity());
        res.setPrice(item.getPrice());

        return res;
    }
}