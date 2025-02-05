package com.example.aquafin.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.aquafin.models.Product;
import com.example.aquafin.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void addProduct(Product product){
         productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @Override
    public void  deleteProductById(Long id){
          // if(!productRepository.existsById(id)){
          //      throw new RuntimeException("Product with ID" + id + "not found");
          // }
         productRepository.deleteById(id);
    }

    @Override
    public void  updateProduct(Product product){
         productRepository.save(product);
    }

    @Override
    public Product getProductById(Long id){
     return productRepository.findById(id).orElse(null);
    }

//     @Override
//     public List<Product> getAvailableProducts(){
//      return productRepository.findByAvailableProductsGreaterThan(0);
//     }





}
