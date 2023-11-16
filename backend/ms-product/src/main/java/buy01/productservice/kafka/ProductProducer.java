package buy01.productservice.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import buy01.productservice.models.Product;

public class ProductProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private KafkaTemplate<String, Product> kafkaTemplateProduct;

    public void deleteProductMedia(String productId) {
        String topic = "delete-product-media";
        kafkaTemplate.send(topic, productId);
    }

    public void itemsSoldNotification(Product product) {
        String topic = "items-sold-notification";
        kafkaTemplateProduct.send(topic, product);
    }
}
