package buy01.userservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import buy01.userservice.models.auth.AuthAuthenticationResponse;
import buy01.userservice.models.auth.AuthRegistrationResponse;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserDetailsResponse(AuthAuthenticationResponse response) {
        kafkaTemplate.send("user-details-response-topic", response);
    }

    public void sendRegistrationResponse(AuthRegistrationResponse response) {
        kafkaTemplate.send("user-registration-response-topic", response);
    }
}

