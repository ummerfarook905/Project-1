package com.example.aquafin.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.aquafin.models.ConfirmOrder;

@Repository
public interface ConfirmOrderRepository extends JpaRepository<ConfirmOrder, Long>{

    List<ConfirmOrder> findByEmail(String email);

}
