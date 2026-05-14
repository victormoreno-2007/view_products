package com.hexagonal.practica.infrastructure.adapters.in.web;

import java.util.List;
import java.util.UUID;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexagonal.practica.domain.model.user.Role;
import com.hexagonal.practica.domain.model.user.User;
import com.hexagonal.practica.domain.ports.in.ManageUserUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    
    private final ManageUserUseCase manageUserUseCase;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
        @RequestBody @Valid UserRequest request
    ) {

        String securePassword = passwordEncoder.encode(request.getPassword());
        User userInput =  User.reconstruct(
            null, 
            request.getFirstname(), 
            request.getLastName(), 
            request.getEmail(), 
            securePassword, 
            Role.USER
        );

        User createUser = manageUserUseCase.create(userInput);

        UserResponse response = new UserResponse(
            createUser.getId(), 
            createUser.getFirstName(), 
            createUser.getLastName(), 
            createUser.getEmail(), 
            createUser.getRole()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        List<User> users = manageUserUseCase.findAll();

        List<UserResponse> responseList = users.stream()
        .map(p -> new UserResponse(
            p.getId(), 
            p.getFirstName(), 
            p.getLastName(), 
            p.getEmail(), 
            p.getRole())
        ).toList();

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id){
        User user = manageUserUseCase.findById(id);

        UserResponse  response = new UserResponse(
            user.getId(), 
            user.getFirstName(), 
            user.getLastName(), 
            user.getEmail(), 
            user.getRole()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUserById(@PathVariable UUID id, @RequestBody UserRequest request){
        User userUpdateInfo = User.reconstruct(
            null, 
            request.getFirstname(), 
            request.getLastName(), 
            request.getEmail(), 
            null, 
            null
        );

        User updateUser = manageUserUseCase.update(id, userUpdateInfo);

        UserResponse response = new UserResponse(
            id, 
            updateUser.getFirstName(), 
            updateUser.getLastName(), 
            updateUser.getEmail(), 
            updateUser.getRole()
        );

        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id){
        manageUserUseCase.deleteById(id);

        return ResponseEntity.noContent().build();
    }



    public static class UserRequest {
    
        private String firstname;
        private String lastName;
        private String email;
        private String password;
        private Role role;

        
        public String getFirstname() {
            return firstname;
        }


        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }


        public String getLastName() {
            return lastName;
        }


        public void setLastName(String lastName) {
            this.lastName = lastName;
        }


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


        public Role getRole() {
            return role;
        }


        public void setRole(Role role) {
            this.role = role;
        }

    }

    public static class UserResponse {
        
        private UUID id;
        private String firstName;
        private String lastName;
        private String email;
        private Role role;
        
        public UserResponse(UUID id, String firstName, String lastName, String email, Role role) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.role = role;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }
        
    }
}
