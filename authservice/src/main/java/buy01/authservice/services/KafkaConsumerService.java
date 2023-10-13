package buy01.authservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import buy01.authservice.models.UserAuthenticationResponse;
import buy01.authservice.models.UserRegistrationResponse;

@Service
public class KafkaConsumerService {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @KafkaListener(topics = "user-registration-response-topic", groupId = "auth-group")
    public void handleUserRegistrationResponse(UserRegistrationResponse response) {
        kafkaProducerService.handleRegistrationResponse(response.getRequestId(), response.isSuccessful());
    }

    @Autowired
    private KafkaUserDetailsService kafkaUserDetailsService; // This service contains your pendingRequests map

    @KafkaListener(topics = "user-details-response-topic", groupId = "auth-group")
    public void handleUserDetailsResponse(UserAuthenticationResponse response) {
        UserDetails userDetails = User.builder()
            .username(response.getUsername())
            .password(response.getPassword())
            .roles(response.getRole().toString())
            .build();

        // Call the method in KafkaUserDetailsService to complete the request
        kafkaUserDetailsService.completeUserDetailsRequest(response.getRequestId(), userDetails);
}
}
