package buy01.userservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import buy01.userservice.models.RegisterRequest;
import buy01.userservice.models.User;
import buy01.userservice.repository.UserRepository;
import buy01.userservice.exceptions.UserNotFoundException;
import jakarta.validation.Valid;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;


@RestController
@RequestMapping("api/user")
public class UserController {

    /* @Autowired
    private UserService userService; */
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/createProfile/{userId}")
    public ResponseEntity<?> createProfile(@PathVariable String userId, @Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User newUser = new User();
            newUser.setId(userId);
            newUser.setUsername(registerRequest.getUsername());
            newUser.setEmail(registerRequest.getEmail());
            newUser.setAvatar(registerRequest.getAvatar());

            // If avatar is not provided, generate a basic avatar with initials
            if (registerRequest.getAvatar() == null || registerRequest.getAvatar().isEmpty()) {
                String backgroundColourHex = String.format("#%06x", new SecureRandom().nextInt(0xffffff + 1));
                String textColourHex = String.format("#%06x", new SecureRandom().nextInt(0xffffff + 1));
                String nameFormatted = registerRequest.getFirstName() + "+" + registerRequest.getLastName();
                // If name is not provided, use username
                 if (nameFormatted.length() < 2) {
                    nameFormatted = registerRequest.getUsername();
                 }
                 newUser.setAvatar("https://ui-avatars.com/api/?name=" + nameFormatted
                                    + "&background=" + backgroundColourHex
                                    + "&color=" + textColourHex);
            }

            newUser.setFirstName(registerRequest.getFirstName());
            newUser.setLastName(registerRequest.getLastName());
            newUser.setStreetAddress(registerRequest.getStreetAddress());
            newUser.setCity(registerRequest.getCity());
            newUser.setState(registerRequest.getState());
            newUser.setZipCode(registerRequest.getZipCode());
            newUser.setPhoneNumber(registerRequest.getPhoneNumber());

            userRepository.save(newUser);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to create profile. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to create profile. Error: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get all users. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to get all users. Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            User user = userRepository.findById(id)
                                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get user by id. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to get user by id. Error: " + e.getMessage());
        }
    }

    @GetMapping("/byName/{name}")
    public ResponseEntity<?> getUserByName(@PathVariable String name) {
        try {
            Optional<User> userOptional = userRepository.findByUsername(name);
            User user = userOptional
                        .orElseThrow(() -> new UserNotFoundException("User not found with username: " + name));
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get user by name. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to get user by name. Error: " + e.getMessage());
        }
    }

    @GetMapping("/byEmail/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            Optional<User> userOptional = userRepository.findUserByEmail(email);
            User user = userOptional
                        .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get user by email. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to get user by email. Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User user = userRepository.findById(id)
                                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

            setIfNotNullOrEmpty(user::setUsername, registerRequest.getUsername());
            setIfNotNullOrEmpty(user::setEmail, registerRequest.getEmail());
            setIfNotNullOrEmpty(user::setAvatar, registerRequest.getAvatar());
            setIfNotNullOrEmpty(user::setFirstName, registerRequest.getFirstName());
            setIfNotNullOrEmpty(user::setLastName, registerRequest.getLastName());
            setIfNotNullOrEmpty(user::setStreetAddress, registerRequest.getStreetAddress());
            setIfNotNullOrEmpty(user::setCity, registerRequest.getCity());
            setIfNotNullOrEmpty(user::setState, registerRequest.getState());
            setIfNotNullOrEmpty(user::setZipCode, registerRequest.getZipCode());
            setIfNotNullOrEmpty(user::setPhoneNumber, registerRequest.getPhoneNumber());

            userRepository.save(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to update user. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to update user. Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to delete user. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to delete user. Error: " + e.getMessage());
        }
    }

    private void setIfNotNullOrEmpty(Consumer<String> setter, String value) {
        if (value != null && !value.isEmpty()) {
            setter.accept(value);
        }
    }
}
