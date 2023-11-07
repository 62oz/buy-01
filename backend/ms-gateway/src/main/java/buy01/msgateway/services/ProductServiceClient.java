package buy01.msgateway.services;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import buy01.msgateway.models.product.ProductRequest;
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

    public List<ProductResponse> getProductsByUserId(String userId) {
        ResponseEntity<List<ProductResponse>> response = restTemplate.getForEntity(
            "http://ms-product/api/product/all/byUserId/" + userId,
            null,
            new ParameterizedTypeReference<List<ProductResponse>>() {});

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to get products by user id");
        }

        return response.getBody();
    }

    public ProductResponse createProduct(ProductRequest productRequest) {
        HttpEntity<ProductRequest> requestEntity = new HttpEntity<>(productRequest);

        ResponseEntity<ProductResponse> response = restTemplate.postForEntity(
            "http://ms-product/api/product/createProduct",
            requestEntity,
            ProductResponse.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to create product");
        }

        return response.getBody();
    }

    public ProductResponse updateProduct(String id, ProductRequest productRequest) {
        HttpEntity<ProductRequest> requestEntity = new HttpEntity<>(productRequest);

        ResponseEntity<ProductResponse> response = restTemplate.exchange(
            "http://ms-product/api/product/updateProduct/" + id,
            HttpMethod.PUT,
            requestEntity,
            ProductResponse.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to update product");
        }

        return response.getBody();
    }

    public void deleteProduct(String id) {
        ResponseEntity<Void> response = restTemplate.exchange(
            "http://ms-product/api/product/deleteProduct/" + id,
            HttpMethod.DELETE,
            null,
            Void.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to delete product");
        }
    }
}
