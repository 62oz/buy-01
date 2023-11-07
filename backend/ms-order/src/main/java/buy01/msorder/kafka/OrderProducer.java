package buy01.msorder.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import buy01.msorder.models.OrderItem;

public class OrderProducer {

    @Autowired
    private KafkaTemplate<String, OrderItem> kafkaTemplate;

    public void updateInventory(OrderItem orderItemRequest) {
        String topic = "update-inventory";
        kafkaTemplate.send(topic, orderItemRequest);
    }
}
