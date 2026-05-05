package com.hexagonal.practica.infrastructure.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexagonal.practica.infrastructure.adapters.out.persistence.entities.UserEntity;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, UUID>{
    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String emial);
}
