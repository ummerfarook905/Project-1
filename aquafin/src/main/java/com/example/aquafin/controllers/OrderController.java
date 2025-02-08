package com.example.aquafin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.aquafin.services.OrderService;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    // @Autowired
    // ProductService productService;

    @PostMapping("/user/addToCart")
    public String addToCart(@RequestParam Long id ,  @RequestParam int quantity, @RequestParam String email){

        // double product = productservice.getProductById(Long id);

        // double totalPrice = orderService.calculateTotalAmount(quantity);

        orderService.addToCart(id, quantity,email);
        return "cart";
    }


}
