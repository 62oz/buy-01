package buy01.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;


@Service
public class KafkaProducerService {

    @Value("${topic.name}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendUserEvent(String message) {
        kafkaTemplate.send(topicName, message);
    }
}
