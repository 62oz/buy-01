package buy01.productservice.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import buy01.productservice.models.OrderItemRequest;
import buy01.productservice.models.OrderRequest;
import buy01.productservice.services.ProductService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductConsumer {

    @Autowired
    private final ProductService productService;

    @KafkaListener(topics = "update-available-quantity", groupId = "product-service")
    public void updateAvailableQuantity(OrderItemRequest orderItemRequest) {
        productService.updateAvailableQuantity(orderItemRequest);
    }

    @KafkaListener(topics = "update-quantity", groupId = "product-service")
    public void updateQuantity (OrderRequest order) {
        productService.updateQuantity(order);
    }

    @KafkaListener(topics = "delete-account", groupId = "product-service")
    public void deleteUserProducts(String accountId) {
        productService.deleteUserProducts(accountId);
    }
}
