package buy01.userservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import buy01.userservice.models.LoginRequest;
import buy01.userservice.models.User;
import buy01.userservice.models.UserAuthenticationResponse;
import buy01.userservice.models.client.ClientResponse;
import buy01.userservice.services.UserService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\")")
    @GetMapping("/")
    public List<ClientResponse> getAll() {
        return userService.getAllUsers();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticationResponse> authenticate(@RequestBody LoginRequest loginRequest) {
        try {
            UserAuthenticationResponse userAuthResponse = userService.authenticateUser(loginRequest);
            return ResponseEntity.ok(userAuthResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\")")
    @GetMapping("/{id}")
    public ClientResponse getById(@PathVariable String id) {
        return userService.getUserById(id);
    }

/*     @PreAuthorize("hasAuthority(\"ROLE_ADMIN\")")
    @GetMapping("/byProductId/{productId}")
    public UserResponse getByProductId(@PathVariable String productId) {
        return userService.getUserByProductId(productId);
    } */

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\") or hasAuthority(\"ROLE_SERVICE\") or #name == principal.name")
    @GetMapping("/byName/{name}")
    public ClientResponse getByName(@PathVariable String username) {
        return userService.getUserByName(username);
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\") or hasAuthority(\"ROLE_SERVICE\") or #email == principal.email")
    @GetMapping("/byEmail/{email}")
    public ClientResponse getByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\") or hasAuthority(\"ROLE_SERVICE\") or #id == principal.id")
    @PostMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody @Valid User updatedUser, Principal principal) {
        try {
            User user = userService.updateUser(id, updatedUser, principal.getName());
            return ResponseEntity.ok(user);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\") or hasAuthority(\"ROLE_SERVICE\") or #id == principal.id")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        userService.deleteUser(id);
    }
}
