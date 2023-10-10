package buy01.authservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "${topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenToUserEvents(String message) {
        System.out.println("Received message: " + message);
        // Handle the message as required
    }
}
