package com.foodapp.controller;

import com.foodapp.model.*;
import com.foodapp.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    public record OrderItemResponse(Long id, String name, String description,
            double unitPrice, int quantity, String imageUrl) {
    }

    public record OrderResponse(Long id, String restaurantName, double totalAmount,
            String paymentMethod, String paymentStatus,
            String deliveryAddress, OrderStatus status,
            LocalDateTime createdAt, List<OrderItemResponse> items) {
    }

    private final FoodOrderRepository orders;
    private final UserRepository users;
    private final RestaurantRepository restaurants;
    private final MenuItemRepository menu;

    public OrderController(FoodOrderRepository orders, UserRepository users,
            RestaurantRepository restaurants, MenuItemRepository menu) {
        this.orders = orders;
        this.users = users;
        this.restaurants = restaurants;
        this.menu = menu;
    }

    public record ItemRequest(Long menuItemId, int quantity) {
    }

    public record OrderRequest(Long restaurantId, String deliveryAddress, String paymentMethod,
            List<ItemRequest> items) {
    }

    @PostMapping
    @Transactional
    public OrderResponse create(@RequestBody OrderRequest req, Authentication auth) {
        User u = users.findByEmail(auth.getName()).orElseThrow();
        Restaurant r = restaurants.findById(req.restaurantId()).orElseThrow();

        FoodOrder o = new FoodOrder();
        o.setCustomer(u);
        o.setRestaurant(r);
        o.setDeliveryAddress(req.deliveryAddress());
        o.setPaymentMethod(req.paymentMethod());

        double total = 0;
        for (ItemRequest ir : req.items()) {
            MenuItem mi = menu.findById(ir.menuItemId()).orElseThrow();
            if (ir.quantity() <= 0)
                throw new IllegalArgumentException("Invalid quantity");
            OrderItem oi = new OrderItem();
            oi.setOrder(o);
            oi.setMenuItem(mi);
            oi.setQuantity(ir.quantity());
            oi.setUnitPrice(mi.getPrice());
            o.getItems().add(oi);
            total += mi.getPrice() * ir.quantity();
        }
        o.setTotalAmount(total);
        if ("TEST".equalsIgnoreCase(req.paymentMethod()))
            o.setPaymentStatus("PAID");
        else if ("COD".equalsIgnoreCase(req.paymentMethod()))
            o.setPaymentStatus("PENDING");
        return toResponse(orders.save(o));
    }

    @GetMapping("/my")
    @Transactional(readOnly = true)
    public List<OrderResponse> my(Authentication auth) {
        User u = users.findByEmail(auth.getName()).orElseThrow();
        return orders.findByCustomerIdOrderByCreatedAtDesc(u.getId()).stream()
                .map(this::toResponse).toList();
    }

    @PostMapping("/{id}/cancel")
    @Transactional
    public OrderResponse cancel(@PathVariable Long id, Authentication auth) {
        FoodOrder order = orders.findByIdWithDetails(id).orElseThrow();
        if (!order.getCustomer().getEmail().equals(auth.getName()))
            throw new org.springframework.security.access.AccessDeniedException("Not allowed");
        if (order.getStatus() != OrderStatus.PLACED && order.getStatus() != OrderStatus.CONFIRMED)
            throw new IllegalStateException("This order can no longer be cancelled");
        order.setStatus(OrderStatus.CANCELLED);
        return toResponse(orders.save(order));
    }

    private OrderResponse toResponse(FoodOrder order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> new OrderItemResponse(item.getId(), item.getMenuItem().getName(),
                        item.getMenuItem().getDescription(), item.getUnitPrice(), item.getQuantity(),
                        item.getMenuItem().getImageUrl()))
                .toList();
        return new OrderResponse(order.getId(), order.getRestaurant().getName(), order.getTotalAmount(),
                order.getPaymentMethod(), order.getPaymentStatus(), order.getDeliveryAddress(),
                order.getStatus(), order.getCreatedAt(), items);
    }

    @GetMapping("/{id}")
    public FoodOrder one(@PathVariable Long id, Authentication auth) {
        FoodOrder o = orders.findById(id).orElseThrow();
        if (!o.getCustomer().getEmail().equals(auth.getName()))
            throw new org.springframework.security.access.AccessDeniedException("Not allowed");
        return o;
    }
}
