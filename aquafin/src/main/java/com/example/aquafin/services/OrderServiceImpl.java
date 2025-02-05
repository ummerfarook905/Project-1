package com.example.aquafin.services;

import java.util.jar.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.aquafin.models.Order;
import com.example.aquafin.models.Product;
import com.example.aquafin.repositories.OrderRepository;

@Service
public class OrderServiceImpl {

    // @Autowired
    // private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepository orderRepository;

    public Order addToCart(Long id,int quantity){
        
        Product product = productService.getProductById(id);

        double totalPrice = product.getPrice() * quantity;

        Order order = new Order();
        order.product(product);
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);

         return orderRepository.save(order);
    }

}
