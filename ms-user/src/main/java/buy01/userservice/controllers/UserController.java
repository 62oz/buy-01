package buy01.userservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import buy01.userservice.models.RegisterRequest;
import buy01.userservice.models.User;
import buy01.userservice.repository.UserRepository;
import buy01.userservice.services.UserService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;
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
}
