package buy01.authservice.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AuthProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void deleteAccount(String accountId) {
        String topic = "delete-account";
        kafkaTemplate.send(topic, accountId);
    }
}
