package com.example.aquafin.services;

import java.util.List;

import com.example.aquafin.Dto.UserDto;
import com.example.aquafin.models.User;

public interface UserService {
    User save(UserDto userDto);
    List<User> getAllUsers();
    List<User> getUsersByRole(String role);
    void deleteUserById(Long id);
    User findByEmail(String email);
}
