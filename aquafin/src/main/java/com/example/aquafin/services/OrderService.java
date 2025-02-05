package com.example.aquafin.services;

import org.springframework.stereotype.Service;


@Service
public interface OrderService {

    void addToCart(Long id, int quantity);

}
