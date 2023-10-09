package buy01.productservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import buy01.productservice.domain.Product;
import buy01.productservice.domain.ProductResponse;
import buy01.productservice.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public List<ProductResponse> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable String id) {
        ProductResponse productResponse = productService.getProductById(id);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/byUserId/{userId}")
    public List<ProductResponse> getByUserId(@PathVariable String userId) {
        return productService.getProductsByUserId(userId);
    }

    @PostMapping("/product")
    public ResponseEntity<?> createProduct(@RequestBody @Valid Product product, Principal principal) {
        try {
            Product createdProduct = productService.createProduct(product, principal.getName());
            return ResponseEntity.ok("Product created: " + createdProduct);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody @Valid Product updatedProduct, Principal principal) {
        try {
            Product product = productService.updateProduct(id, updatedProduct, principal.getName());
            return ResponseEntity.ok("Product updated: " + product);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id, Principal principal) {
        try {
            productService.deleteProduct(id, principal.getName());
            return ResponseEntity.ok("Product deleted");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }
}
