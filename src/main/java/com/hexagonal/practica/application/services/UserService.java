package com.hexagonal.practica.application.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hexagonal.practica.domain.exception.DomainException;
import com.hexagonal.practica.domain.model.user.User;
import com.hexagonal.practica.domain.ports.in.ManageUserUseCase;
import com.hexagonal.practica.domain.ports.out.UserRepositoryPort;

@Service
public class UserService implements ManageUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public UserService(UserRepositoryPort userRepositoryPort){
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public User create(User user) {
        if (userRepositoryPort.exist(user.getEmail())) {
            throw new DomainException("Usuario ya existe con este correo");
        }
        return userRepositoryPort.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepositoryPort.findAll();
    }

    @Override
    public User findById(UUID id) {
        return userRepositoryPort.findById(id).orElseThrow(() -> new DomainException("Usuario no encontrado"));
    }

    @Override
    public User update(UUID id, User user) {
        User existUser = findById(id);

        existUser.updatePersonalInfo(
            user.getFirstName(), 
            user.getLastName());

        return userRepositoryPort.save(existUser);


    }

    @Override
    public void deleteById(UUID id) {
        userRepositoryPort.deleteById(id);
    }
    
}
