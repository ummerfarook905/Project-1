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

    @PostMapping("user-addToCart")
    public String addToCart(@RequestParam Long id ,  @RequestParam int quantity){

        orderService.addToCart(id, quantity);
        return "redirect:/cart/view";

    }


}
