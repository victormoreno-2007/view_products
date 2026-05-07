package com.hexagonal.practica.infrastructure.adapters.in.web;

public class LoginResquest {
    private String email;
    private String password;
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginResquest() {
    }

    public LoginResquest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    
}
