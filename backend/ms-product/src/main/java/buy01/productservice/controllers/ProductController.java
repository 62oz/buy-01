package buy01.productservice.controllers;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import buy01.productservice.models.OrderItemRequest;
import buy01.productservice.models.Product;
import buy01.productservice.models.ProductRequest;
import buy01.productservice.services.ProductService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        try {
            List<Product> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get all products. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to get all products. Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get product by id. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to get product by id. Error: " + e.getMessage());
        }
    }

    @GetMapping("/all/byUserId/{userId}")
    public ResponseEntity<?> getProductsByUserId(@PathVariable String userId) {
        try {
            List<Product> products = productService.getProductsByUserId(userId);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get products by user id. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to get products by user id. Error: " + e.getMessage());
        }
    }

    @PostMapping("/createProduct")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest) {
        try {
            Product newProduct = productService.createProduct(productRequest);
            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to create product. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to create product. Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\") or @productService.isOwner(#id, principal.id)")
    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id,
                                                @RequestBody ProductRequest productRequest) {
        try {
            Product updatedProduct = productService.updateProduct(id, productRequest);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to update product. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to update product. Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\") or @productService.isOwner(#id, principal.id)")
    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to delete product. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to delete product. Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-account-products/{userId}")
    public ResponseEntity<?> deleteAccountProducts(@PathVariable String userId) {
        try {
            productService.deleteUserProducts(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to delete account products. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to delete account products. Error: " + e.getMessage());
        }
    }

    @GetMapping("/check-availabiliy")
    public ResponseEntity<?> checkAvailability(@RequestBody OrderItemRequest orderItem) {
        try {
            Product product = productService.getProductById(orderItem.getProductId());
            return ResponseEntity.ok(product.getAvailableQuantity() >= orderItem.getQuantity());
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to check availability. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to check availability. Error: " + e.getMessage());
        }
    }
}
