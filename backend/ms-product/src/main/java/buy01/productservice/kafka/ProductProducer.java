package buy01.productservice.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

public class ProductProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void deleteProduct(String productId) {
        String topic = "delete-product";
        kafkaTemplate.send(topic, productId);
    }
}
