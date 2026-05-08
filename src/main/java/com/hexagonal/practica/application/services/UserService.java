package com.hexagonal.practica.application.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hexagonal.practica.domain.exception.BusinessErrorCode;
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
            throw new DomainException(BusinessErrorCode.USER_NOT_FOUND);
        }
        return userRepositoryPort.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepositoryPort.findAll();
    }

    @Override
    public User findById(UUID id) {
        return userRepositoryPort.findById(id).orElseThrow(() -> new DomainException(BusinessErrorCode.USER_NOT_FOUND));
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

    @Override
    public User findByEmail(String email) {
        return userRepositoryPort.findByEmail(email).orElse(null);
    }

    

   
    
}
