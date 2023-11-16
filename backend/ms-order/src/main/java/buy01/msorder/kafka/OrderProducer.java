package buy01.msorder.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import buy01.msorder.models.Order;
import buy01.msorder.models.OrderItem;

public class OrderProducer {

    @Autowired
    private KafkaTemplate<String, OrderItem> kafkaTemplateOrderItem;
    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplateOrder;

    public void updateAvailableQuantity(OrderItem orderItemRequest) {
        String topic = "update-available-quantity";
        kafkaTemplateOrderItem.send(topic, orderItemRequest);
    }

    public void updateQuantity(Order order) {
        String topic = "update-quantity";
        kafkaTemplateOrder.send(topic, order);
    }
}
