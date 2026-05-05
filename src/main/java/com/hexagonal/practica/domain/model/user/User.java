package com.hexagonal.practica.domain.model.user;

import java.util.UUID;

import com.hexagonal.practica.domain.exception.DomainException;

public class User {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;

    
    private User(){}

    public static User create(String email, String password, String firstName, String lastname, Role role){
        if (email == null || !email.contains("@")) {
            throw new DomainException("Email invalido, por favor verifica");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new DomainException("Error al ingresar la contraseña, por favor verifica");
        }

        User user = new User();
        user.id = UUID.randomUUID();
        user.firstName = firstName;
        user.lastName = lastname;
        user.email = email;
        user.password = password;
        user.role = role;

        return user;
    }

    public static User reconstruct(UUID id, String firstName, String lastName, String email, String password, Role role){
        User user = new User();
        user.id = id;
        user.firstName = firstName;
        user.lastName = lastName;
        user.email = email;
        user.password = password;
        user.role = role;

        return user;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public void updatePersonalInfo(String firstName, String lastname){
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new DomainException("Campo no puede ser vacío");
        }

        if (lastname == null || lastname.trim().isEmpty()) {
            throw new DomainException("Campo no puede ser vacío");
        }

        this.firstName = firstName;
        this.lastName = lastname;
    }

    public void changepassword(String newPassword){
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new DomainException("la nueva contraseña debe de tener contenido");
        }
    }

    
}
