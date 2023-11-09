package buy01.productservice.services;

import org.springframework.stereotype.Service;

import buy01.productservice.kafka.ProductProducer;
import buy01.productservice.models.OrderItemRequest;
import buy01.productservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductProducer productProducer;

    public Boolean isOwner(String productId, String authenticatedId) {
        return productRepository.findById(productId)
                .map(product -> product.getUserId().equals(authenticatedId))
                .orElse(false);
    }

    public void updateInventory(OrderItemRequest orderItemRequest) {
        String productId = orderItemRequest.getProductId();
        Integer quantity = orderItemRequest.getQuantity();
        productRepository.findById(productId)
                .map(product -> {
                    product.setAvailableQuantity(product.getAvailableQuantity() - quantity);
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found for id:" + productId));
    }

    public void deleteUserProducts(String accountId) {
        productProducer.deleteProduct(accountId);
        productRepository.deleteByUserId(accountId);
    }
}
