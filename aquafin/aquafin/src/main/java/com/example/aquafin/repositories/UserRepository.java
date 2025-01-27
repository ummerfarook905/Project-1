package com.example.aquafin.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.aquafin.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findByRole(String role);
    // List<User> findByRoleIn(List<String> roles);   
    
}
