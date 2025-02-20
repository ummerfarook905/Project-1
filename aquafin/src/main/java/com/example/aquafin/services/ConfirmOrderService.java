package com.example.aquafin.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.aquafin.models.ConfirmOrder;
import com.example.aquafin.models.Order;
import com.example.aquafin.repositories.ConfirmOrderRepository;

import jakarta.transaction.Transactional;



@Service
public class ConfirmOrderService {

    @Autowired
    private ConfirmOrderRepository confirmOrderRepository;

    @Autowired
    private OrderService orderService;

    @Transactional
    public void createConfirmOrdersFromCart(String email) {
        // Get cart orders
        List<Order> cartOrders = orderService.getOrdersByEmail(email);
        
        // Convert each cart order to confirm order
        List<ConfirmOrder> confirmOrders = cartOrders.stream()
            .map(order -> {
                ConfirmOrder confirmOrder = new ConfirmOrder();
                confirmOrder.setEmail(email);
                confirmOrder.setProductName(order.getProductName());
                confirmOrder.setProductId(order.getProductId());
                confirmOrder.setQuantity(order.getQuantity());
                confirmOrder.setTotalPrice(order.getTotalPrice());
                confirmOrder.setOrderDate(LocalDateTime.now());
                return confirmOrder;
            })
            .collect(Collectors.toList());

        // Save all confirm orders
        confirmOrderRepository.saveAll(confirmOrders);
        
        // Clear the cart (delete orders)
        orderService.deleteOrdersByEmail(email);
    }


}
