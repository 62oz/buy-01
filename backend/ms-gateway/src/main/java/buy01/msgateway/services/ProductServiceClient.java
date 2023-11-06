package buy01.msgateway.services;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import buy01.msgateway.models.product.ProductResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceClient {

    private final RestTemplate restTemplate;

    public List<ProductResponse> getAllProducts() {
        ResponseEntity<List<ProductResponse>> response = restTemplate.getForEntity(
            "http://ms-product/api/product/all",
            null,
            new ParameterizedTypeReference<List<ProductResponse>>() {});

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to get all products");
        }

        return response.getBody();
    }

    public ProductResponse getProductById(String id) {
        ResponseEntity<ProductResponse> response = restTemplate.getForEntity(
            "http://ms-product/api/product/" + id,
            null,
            ProductResponse.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to get product by id");
        }

        return response.getBody();
    }
}
