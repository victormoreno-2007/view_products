package com.hexagonal.practica.infrastructure.adapters.in.web;

import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexagonal.practica.domain.model.user.User;
import com.hexagonal.practica.domain.ports.in.ManageUserUseCase;
import com.hexagonal.practica.infrastructure.security.JwtService;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class  AuthController {

    private final ManageUserUseCase manageUserUseCase;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

   private final Bucket bucket = Bucket.builder()
            .addLimit(Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1))))
            .build();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        User user = manageUserUseCase.findByEmail(request.email());

        if (bucket.tryConsume(1)) {
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            if (!passwordEncoder.matches(request.password(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String token = jwtService.generateToken(user);

            return ResponseEntity.ok(new AuthResponse(token));
        } else{
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Has excedido el limite de intentos.");
        }

        
    }




    public record LoginRequest(String email, String password) {
    }

    public record AuthResponse(String token) {
    }
}
