package buy01.productservice.services;

import org.springframework.stereotype.Service;

import buy01.productservice.kafka.ProductProducer;
import buy01.productservice.models.OrderItemRequest;
import buy01.productservice.repositories.ProductRepository;
import buy01.productservice.models.Product;
import buy01.productservice.models.ProductRequest;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductProducer productProducer;
    private final JwtService jwtService;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByUserId(String userId) {
        return productRepository.findByUserId(userId)
                .stream()
                .map(Optional::get)
                .toList();
    }

    public Product createProduct(ProductRequest productRequest) {
        String userId = jwtService.getAuthenticatedId();
        Product product = new Product();
        product.setUserId(userId);
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setAvailableQuantity(productRequest.getAvailableQuantity());
        return productRepository.save(product);
    }

    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found for id:" + id));
    }

    public Boolean isOwner(String productId, String authenticatedId) {
        return productRepository.findById(productId)
                .map(product -> product.getUserId().equals(authenticatedId))
                .orElse(false);
    }

    public Product updateProduct(String productId, ProductRequest productRequest) {
        return productRepository.findById(productId)
                .map(product -> {
                    setIfNotNullOrEmptyString(product::setName, productRequest.getName());
                    setIfNotNullOrEmptyString(product::setDescription, productRequest.getDescription());
                    setIfNotNullOrEmptyInteger(product::setQuantity, productRequest.getQuantity());
                    setIfNotNullOrEmptyInteger(product::setAvailableQuantity, productRequest.getAvailableQuantity());
                    setIfNotNullOrEmptyBigDecimal(product::setPrice, productRequest.getPrice());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found for id:" + productId));
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

    public void deleteProduct(String productId) {
        productProducer.deleteProduct(productId);
        productRepository.deleteById(productId);
    }

    public void deleteUserProducts(String accountId) {
        List<Optional<Product>> productsOptionals = productRepository.findByUserId(accountId);
        productsOptionals.forEach(productOptional -> {
            productOptional.ifPresent(product -> {
                productRepository.deleteById(product.getId());
                productProducer.deleteProduct(product.getId());
            });
        });
    }

    private void setIfNotNullOrEmptyString(Consumer<String> setter, String value) {
        if (value != null && !value.isEmpty()) {
            setter.accept(value);
        }
    }

    private void setIfNotNullOrEmptyInteger(Consumer<Integer> setter, Integer value) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private void setIfNotNullOrEmptyBigDecimal(Consumer<BigDecimal> setter, BigDecimal value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
