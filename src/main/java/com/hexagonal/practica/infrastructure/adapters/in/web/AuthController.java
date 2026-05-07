package com.hexagonal.practica.infrastructure.adapters.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexagonal.practica.domain.model.user.User;
import com.hexagonal.practica.domain.ports.in.ManageUserUseCase;
import com.hexagonal.practica.infrastructure.security.JwtService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class  AuthController {

    private final ManageUserUseCase manageUserUseCase;
    private final JwtService jwtService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        User user = manageUserUseCase.findByEmail(request.email());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!user.getPassword().equals(request.password())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(token));
    }




    public record LoginRequest(String email, String password) {
    }

    public record AuthResponse(String token) {
    }
}
