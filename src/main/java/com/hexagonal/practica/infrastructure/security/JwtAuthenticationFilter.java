package com.hexagonal.practica.infrastructure.security;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexagonal.practica.domain.model.user.User;
import com.hexagonal.practica.domain.ports.in.ManageUserUseCase;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final ManageUserUseCase manageUserUseCase;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain)
        throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        try {
            final String userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = manageUserUseCase.findByEmail(userEmail);

                if (user != null && jwtService.isTokenValid(jwt, user)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user.getId(), 
                            null, 
                            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            log.warn("JWT expirado para el request: {} {}", request.getMethod(), request.getRequestURI());
            sendUnauthorizedResponse(response, "El token ha expirado. Por favor, inicie sesión nuevamente.");

        } catch (JwtException ex) {
            log.warn("JWT inválido para el request: {} {} - {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
            sendUnauthorizedResponse(response, "Token inválido. Por favor, inicie sesión nuevamente.");
        }
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("error", "Unauthorized");
        errorBody.put("message", message);
        errorBody.put("status", "401");

        objectMapper.writeValue(response.getOutputStream(), errorBody);
    }
}
