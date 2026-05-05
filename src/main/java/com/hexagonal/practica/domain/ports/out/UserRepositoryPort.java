package com.hexagonal.practica.domain.ports.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.hexagonal.practica.domain.model.user.User;

public interface UserRepositoryPort {
    User save(User user);
    List<User> findAll();
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    boolean exist(String email);
    void deleteById(UUID id);
}
