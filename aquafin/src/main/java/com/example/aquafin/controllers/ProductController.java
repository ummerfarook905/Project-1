package com.example.aquafin.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.aquafin.models.Product;
import com.example.aquafin.services.ProductService;


@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/super-admin/add-product")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public  String getAddProduct(Product product){
        return "add-product";
    }

    @PostMapping("/super-admin/add-product")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String addProduct(Product product){
        productService.addProduct(product);
        return "redirect:/super-admin";
    }

    @GetMapping("/super-admin/view-products")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String viewProducts(Model model){
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products",products);
        return "view-products";
    }

    @GetMapping("/super-admin/update-product/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String getEditProductPage(Model model){
        return "update-product";
    }

    @PostMapping("/super-admin/update-product/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String updateProduct(@PathVariable Long id,@ModelAttribute Product product,Model model){
        try {
            product.setId(id);
            productService.updateProduct(product);
            return "redirect:/super-admin/view-products";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update product");
            model.addAttribute("product", product);
            return "update-product";
        }
    }
}
