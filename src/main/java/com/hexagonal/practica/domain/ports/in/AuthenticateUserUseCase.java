package com.hexagonal.practica.domain.ports.in;

import com.hexagonal.practica.domain.model.user.User;

public interface AuthenticateUserUseCase {

    User authenticate(String email, String password);
    
}
