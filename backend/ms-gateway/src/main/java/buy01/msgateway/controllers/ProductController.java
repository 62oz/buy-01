package buy01.msgateway.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import buy01.msgateway.models.product.ProductRequest;
import buy01.msgateway.models.product.ProductResponse;
import buy01.msgateway.services.ProductServiceClient;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductServiceClient productServiceClient;

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productServiceClient.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(productServiceClient.getProductById(id));
    }

    @GetMapping("/all/byUserId/{userId}")
    public ResponseEntity<List<ProductResponse>> getProductsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(productServiceClient.getProductsByUserId(userId));
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\") or hasAuthority(\"ROLE_SELLER\")")
    @PostMapping("/createProduct")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productServiceClient.createProduct(productRequest));
    }

    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable String id,
                                                    @RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productServiceClient.updateProduct(id, productRequest));
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productServiceClient.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
