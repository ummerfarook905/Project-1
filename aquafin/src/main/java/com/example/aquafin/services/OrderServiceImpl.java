package com.example.aquafin.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.aquafin.models.Order;
import com.example.aquafin.models.Product;
import com.example.aquafin.repositories.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

    // @Autowired
    // private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void  addToCart(Long id,int quantity,String email){

        Product product = productService.getProductById(id);

        float totalPrice = product.getPrice(id) * quantity;

        Order order = new Order();
        order.setEmail(email);
        order.setProduct(product);
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);

        orderRepository.save(order);
    }

    

}
