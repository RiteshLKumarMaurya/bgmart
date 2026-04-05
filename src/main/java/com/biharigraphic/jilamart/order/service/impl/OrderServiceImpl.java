package com.biharigraphic.jilamart.order.service.impl;

import com.biharigraphic.jilamart.cart.repo.CartRepository;
import com.biharigraphic.jilamart.enums.OrderStatus;
import com.biharigraphic.jilamart.cart.entity.Cart;
import com.biharigraphic.jilamart.entity.CartItem;
import com.biharigraphic.jilamart.entity.OrderItem;
import com.biharigraphic.jilamart.notification.service.NotificationService;
import com.biharigraphic.jilamart.order.dto.CheckoutRequest;
import com.biharigraphic.jilamart.order.dto.OrderResponse;
import com.biharigraphic.jilamart.order.entity.OrderEntity;
import com.biharigraphic.jilamart.order.mapper.OrderMapper;
import com.biharigraphic.jilamart.order.repo.OrderRepository;
import com.biharigraphic.jilamart.order.service.OrderService;
import com.biharigraphic.jilamart.product.entity.Product;
import com.biharigraphic.jilamart.role.enums.RoleName;
import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public OrderResponse checkout(Long userId, CheckoutRequest request) {

        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        OrderEntity order = new OrderEntity();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(Instant.now());
        order.setAddress(request.getAddress());
        order.setPhone(request.getPhone());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getCartItems()) {

            Product product = cartItem.getProduct();

            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Stock not available for " + product.getName());
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(product.getPrice());
            item.setProductName(product.getName());

            orderItems.add(item);

            total = total.add(
                    product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))
            );
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);
        order.setFinalAmount(total);

        orderRepository.save(order);

        // 🔥 USER NOTIFICATION
        notificationService.sendToUser(
                order.getUser(),
                "Order Confirmed ✅",
                "Your order has been placed successfully"
        );

        // 🔥 ADMIN NOTIFICATION
        List<User> admins = userRepository.findByRole_Name(RoleName.ROLE_ADMIN);

        for (User admin : admins) {
            notificationService.sendToUser(
                    admin,
                    "New Order 🔥",
                    "Order received from user: " + order.getUser().getFullName()
            );
        }

        return orderMapper.toResponse(order);
    }
    @Override
    public OrderResponse getOrder(Long orderId) {

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return orderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getUserOrders(Long userId) {

        List<OrderEntity> orders = orderRepository.findByUserId(userId);

        return orders.stream()
                .map(orderMapper::toResponse)
                .toList();
    }
}