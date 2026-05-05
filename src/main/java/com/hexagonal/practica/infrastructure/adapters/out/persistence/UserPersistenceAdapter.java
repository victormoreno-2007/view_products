package com.hexagonal.practica.infrastructure.adapters.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.hexagonal.practica.domain.model.user.User;
import com.hexagonal.practica.domain.ports.out.UserRepositoryPort;
import com.hexagonal.practica.infrastructure.adapters.out.persistence.entities.UserEntity;
import com.hexagonal.practica.infrastructure.adapters.out.persistence.mappers.UserMapper;
import com.hexagonal.practica.infrastructure.repositories.SpringDataUserRepository;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort{

    private final SpringDataUserRepository springDataUserRepository;

    private final UserMapper userMapper;



    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);

        return userMapper.toDomain(springDataUserRepository.save(entity));
    }

    @Override
    public List<User> findAll() {
        return springDataUserRepository.findAll().stream()
        .map(userMapper::toDomain)
        .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(UUID id) {
        return springDataUserRepository.findById(id)
        .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataUserRepository.findByEmail(email)
        .map(userMapper::toDomain);
    }

    @Override
    public boolean exist(String email) {
        return springDataUserRepository.existsByEmail(email);
    }

    @Override
    public void deleteById(UUID id) {
        springDataUserRepository.deleteById(id);
    }
    
}
