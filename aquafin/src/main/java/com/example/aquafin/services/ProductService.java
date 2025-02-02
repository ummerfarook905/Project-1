package com.example.aquafin.services;

import java.util.List;

import com.example.aquafin.models.Product;

public interface ProductService {

    void addProduct(Product product);

    List<Product> getAllProducts();

    void deleteProduct(Long id);

    void updateProduct(Product product);
    
    Product getProductById(Long id);

}
