package buy01.authservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import buy01.authservice.models.user.UserAuthenticationResponse;
import buy01.authservice.models.user.UserRegistrationResponse;

@Service
public class KafkaConsumerService {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @KafkaListener(topics = "user-registration-response-topic", groupId = "auth-group")
    public void handleUserRegistrationResponse(UserRegistrationResponse response) {
        kafkaProducerService.handleRegistrationResponse(response.getRequestId(), response.getUserId());
    }

    @KafkaListener(topics = "user-details-response-topic", groupId = "auth-group")
    public void handleUserAuthenticationResponse(UserAuthenticationResponse response) {
        kafkaProducerService.handleAuthenticationResponse(response.getRequestId(), response);
    }
}
