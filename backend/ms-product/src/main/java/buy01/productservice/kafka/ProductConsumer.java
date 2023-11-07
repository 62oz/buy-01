package buy01.productservice.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import buy01.productservice.models.OrderItemRequest;
import buy01.productservice.services.ProductService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductConsumer {

    @Autowired
    private final ProductService productService;

    @KafkaListener(topics = "update-inventory", groupId = "product-service")
    public void updateInventory(OrderItemRequest orderItemRequest) {
        productService.updateInventory(orderItemRequest);
    }
}
