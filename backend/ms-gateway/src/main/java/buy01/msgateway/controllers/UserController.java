package buy01.msgateway.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import buy01.msgateway.models.auth.RegisterRequest;
import buy01.msgateway.models.user.UserResponse;
import buy01.msgateway.services.UserServiceClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserServiceClient userServiceClient;

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\")")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userServiceClient.getAllUsers());
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\")")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userServiceClient.getUserById(id));
    }

    // IMPLEMENT GET USER BY PRODUCT ID

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\") or #name == principal.name")
    @GetMapping("/byName/{name}")
    public ResponseEntity<UserResponse> getUserByName(@PathVariable String name) {
        return ResponseEntity.ok(userServiceClient.getUserByName(name));
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\") or #email == principal.email")
    @GetMapping("/byEmail/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userServiceClient.getUserByEmail(email));
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\") or #id == principal.id")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable String id, @Valid @RequestBody RegisterRequest registerRequest) {
        userServiceClient.updateUser(id, registerRequest);
        return ResponseEntity.ok().build();
    }
}
