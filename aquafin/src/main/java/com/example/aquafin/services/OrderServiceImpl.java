package com.example.aquafin.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.aquafin.models.Order;
import com.example.aquafin.models.Product;
import com.example.aquafin.repositories.OrderRepository;

@Service
public class OrderServiceImpl implements ProductService {

    // @Autowired
    // private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepository orderRepository;

    public Order addToCart(Long id,int quantity,String email){

        Product product = productService.getProductById(id);

        float totalPrice = product.getPrice() * quantity;

        Order order = new Order();
        order.setEmail(email);
        order.setProduct(product);
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);

         return orderRepository.save(order);
    }

}
