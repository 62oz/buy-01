package buy01.userservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import buy01.userservice.models.AuthRegistrationRequest;
import buy01.userservice.models.AuthRegistrationResponse;
import buy01.userservice.models.ClientAuthenticationRequest;
import buy01.userservice.models.User;
import buy01.userservice.models.UserDetailsKafkaResponse;
import buy01.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    @Autowired
    private UserRepository userRepository;

    private final KafkaTemplate<String, UserDetailsKafkaResponse> userDetailsKafkaTemplate;

    @KafkaListener(topics = "user-details-request-topic", groupId = "userservice-group")
    public void handleUserDetailsRequest(ClientAuthenticationRequest request) {
        try {
            User user = userRepository.findByName(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UserDetails userDetails = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .build();

            UserDetailsKafkaResponse response = UserDetailsKafkaResponse.builder()
                .requestId(request.getRequestId())
                .userDetails(userDetails)
                .build();

            userDetailsKafkaTemplate.send("user-details-response-topic", response);
        } catch (RuntimeException ex) {
            UserDetailsKafkaResponse errorResponse = UserDetailsKafkaResponse.builder()
                .requestId(request.getRequestId())
                .userDetails(null)
                .build();
            userDetailsKafkaTemplate.send("user-details-response-topic", errorResponse);
        }
    }

    private final KafkaTemplate<String, AuthRegistrationResponse> registrationKafkaTemplate;


    @KafkaListener(topics = "user-registration-requests", groupId = "userservice-group")
    public void handleUserRegistrationRequest(AuthRegistrationRequest dto) {
        // Handle user registration logic here
        // For example, save the user to the database
        User newUser = new User();
        newUser.setUsername(dto.getUsername());
        newUser.setEmail(dto.getEmail());
        newUser.setPassword(dto.getPassword());
        newUser.setSalt(dto.getSalt());
        newUser.setRole(dto.getRole());
        newUser.setAvatar(dto.getAvatar());

        userRepository.save(newUser);

        AuthRegistrationResponse response = new AuthRegistrationResponse(
            dto.getRequestId(),
            true // REPLACE to handle if registration was not successful
        );
        registrationKafkaTemplate.send("user-registration-response-topic", response);
    }
}