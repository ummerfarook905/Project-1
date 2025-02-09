package com.example.aquafin.controllers;

import java.security.Principal;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.aquafin.models.Order;
import com.example.aquafin.services.OrderService;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    // @Autowired
    // ProductService productService;

    @PostMapping("/user/addToCart")
    public String addToCart(@RequestParam Long id ,  @RequestParam int quantity,Principal principal){

        String email = principal.getName();

        // double product = productservice.getProductById(Long id);

        // double totalPrice = orderService.calculateTotalAmount(quantity);

        orderService.addToCart(id, quantity,email);
        return "cart";
    }
    @GetMapping("/cart")
    public String viewCart(Model model,Principal principal) {
        String email = principal.getName();
        List<Order> orders = orderService.getOrdersByEmail(email);
        model.addAttribute("orders", orders);
        return "cart";
}


}
