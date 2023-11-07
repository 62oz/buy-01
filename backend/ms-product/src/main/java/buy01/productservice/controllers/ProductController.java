package buy01.productservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Consumer;

import buy01.productservice.models.OrderItemRequest;
import buy01.productservice.models.Product;
import buy01.productservice.models.ProductRequest;
import buy01.productservice.repositories.ProductRepository;
import buy01.productservice.services.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private final JwtService jwtService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        try {
            List<Product> products = productRepository.findAll();
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
            Product product = productRepository.findById(id)
                                            .orElseThrow(() -> new Exception("Product not found with id: " + id));
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
            List<Product> products = productRepository.findByUserId(userId);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get products by user id. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to get products by user id. Error: " + e.getMessage());
        }
    }

    @PostMapping("/createProduct")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest,
                                                @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String jwt = authorizationHeader.substring(7);
            // ADD error handling I guess
            String userId = jwtService.extractUserId(jwt);
            Product newProduct = new Product();
            newProduct.setName(productRequest.getName());
            newProduct.setDescription(productRequest.getDescription());
            newProduct.setPrice(productRequest.getPrice());
            newProduct.setQuantity(productRequest.getQuantity());
            newProduct.setUserId(userId);

            productRepository.save(newProduct);
            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to create product. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to create product. Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\") or @productService.isOwner(#id, authentication.principal.id)")
    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id,
                                                @RequestBody ProductRequest productRequest) {
        try {
            Product product = productRepository.findById(id)
                                            .orElseThrow(() -> new Exception("Product not found with id: " + id));
            setIfNotNullOrEmptyString(product::setName, productRequest.getName());
            setIfNotNullOrEmptyString(product::setDescription, productRequest.getDescription());
            setIfNotNullOrEmptyDouble(product::setPrice, productRequest.getPrice());
            setIfNotNullOrEmptyInteger(product::setQuantity, productRequest.getQuantity());

            productRepository.save(product);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to update product. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to update product. Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\") or @productService.isOwner(#id, authentication.principal.id)")
    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        try {
            productRepository.deleteById(id);
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
            List<Product> products = productRepository.findByUserId(userId);
            productRepository.deleteAll(products);
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
            Product product = productRepository.findById(orderItem.getProductId())
                                            .orElseThrow(() -> new Exception("Product not found with id: " + orderItem.getProductId()));
            if (product.getQuantity() >= orderItem.getQuantity()) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body("Product with id: " + orderItem.getProductId() + " is not available in the requested quantity");
            }
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to check availability. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to check availability. Error: " + e.getMessage());
        }
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

    private void setIfNotNullOrEmptyDouble(Consumer<Double> setter, Double value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
