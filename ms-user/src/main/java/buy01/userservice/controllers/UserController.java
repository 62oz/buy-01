package buy01.userservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import buy01.userservice.models.RegisterRequest;
import buy01.userservice.models.User;
import buy01.userservice.repository.UserRepository;
import buy01.userservice.exceptions.UserNotFoundException;
import jakarta.validation.Valid;
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
    public ResponseEntity<Void> createProfile(@PathVariable String userId, @Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User newUser = new User();
            newUser.setId(userId);
            newUser.setUsername(registerRequest.getUsername());
            newUser.setEmail(registerRequest.getEmail());
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
            System.out.println("Failed to create profile");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get all users");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        try {
            User user = userRepository.findById(id)
                                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get user by id");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byName/{name}")
    public ResponseEntity<User> getUserByName(@PathVariable String name) {
        try {
            Optional<User> userOptional = userRepository.findByUsername(name);
            User user = userOptional
                        .orElseThrow(() -> new UserNotFoundException("User not found with username: " + name));
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get user by name");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byEmail/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        try {
            Optional<User> userOptional = userRepository.findUserByEmail(email);
            User user = userOptional
                        .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get user by email");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable String id, @Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User user = userRepository.findById(id)
                                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

            setIfNotNullOrEmpty(user::setUsername, registerRequest.getUsername());
            setIfNotNullOrEmpty(user::setEmail, registerRequest.getEmail());
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
            System.out.println("Failed to update user");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        try {
            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to delete user");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void setIfNotNullOrEmpty(Consumer<String> setter, String value) {
        if (value != null && !value.isEmpty()) {
            setter.accept(value);
        }
    }
}
