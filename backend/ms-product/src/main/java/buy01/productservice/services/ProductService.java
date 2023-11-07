package buy01.productservice.services;

import org.springframework.stereotype.Service;

import buy01.productservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Boolean isOwner(String productId, String authenticatedId) {
        return productRepository.findById(productId)
                .map(product -> product.getUserId().equals(authenticatedId))
                .orElse(false);
    }
}
