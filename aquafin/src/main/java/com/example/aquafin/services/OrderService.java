package com.example.aquafin.services;


import java.util.List;

import org.springframework.stereotype.Service;

import com.example.aquafin.models.Order;


@Service
public interface OrderService {

    void addToCart(Long id, int quantity,String email);

    List<Order> getOrders();

    List<Order> getOrdersByEmail(String email);

    // double calculateTotalAmount(Long id,int quantity);

    // double getProductPrice(Long id);

}
