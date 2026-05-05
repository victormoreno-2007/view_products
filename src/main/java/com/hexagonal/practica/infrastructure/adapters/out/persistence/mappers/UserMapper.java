package com.hexagonal.practica.infrastructure.adapters.out.persistence.mappers;

import org.springframework.stereotype.Component;

import com.hexagonal.practica.domain.model.user.User;
import com.hexagonal.practica.infrastructure.adapters.out.persistence.entities.UserEntity;


@Component
public class UserMapper {

    public User toDomain(UserEntity entity){
        if (entity == null) {
            return null;
        }

        return User.reconstruct(
            entity.getId(), 
            entity.getFirstName(), 
            entity.getLastname(), 
            entity.getEmail(), 
            entity.getPassword(), 
            entity.getRole());
    }

    public UserEntity toEntity(User domain){
        if (domain == null) {
            return null;
        }

        return UserEntity.builder()
        .id(domain.getId())
        .firstName(domain.getFirstName())
        .lastname(domain.getLastName())
        .email(domain.getEmail())
        .password(domain.getPassword())
        .role(domain.getRole())
        .build();
    }
    
}
