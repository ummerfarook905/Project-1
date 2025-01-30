package com.example.aquafin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.aquafin.models.Product;
import com.example.aquafin.services.ProductService;


@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/super-admin/add-product")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public  String getAddProduct(Model model){
        return "add-product";
    }

    @PostMapping("/super-admin/add-product")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String addProduct(Product product){
        productService.addProduct(product);
        return "redirect:/super-admin";
    }
}
