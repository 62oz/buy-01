package buy01.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, UserDto> kafkaTemplate;

    public void sendRegistrationRequest(UserDto userDto) {
        kafkaTemplate.send("user-registration-requests", userDto);
    }
}
