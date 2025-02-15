package com.example.aquafin.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.aquafin.models.Order;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByEmail(String email);
    

}
