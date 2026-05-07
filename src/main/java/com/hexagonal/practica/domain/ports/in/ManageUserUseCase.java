package com.hexagonal.practica.domain.ports.in;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.hexagonal.practica.domain.model.user.User;

public interface ManageUserUseCase {
    User create(User user);
    List<User> findAll();
    User findByEmail(String email);
    User findById(UUID id);
    User update(UUID id, User user);
    void deleteById(UUID id); 
}
