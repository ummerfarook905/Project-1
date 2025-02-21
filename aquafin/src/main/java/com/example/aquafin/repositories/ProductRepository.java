package com.example.aquafin.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.aquafin.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> searchProducts(@Param("name") String name);


    //  List<Product> findByAvailableProductsGreaterThan(int quantity);
}
