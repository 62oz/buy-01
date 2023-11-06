package buy01.productservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Consumer;

import buy01.productservice.models.Product;
import buy01.productservice.models.ProductRequest;
import buy01.productservice.repositories.ProductRepository;
import buy01.productservice.services.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.http.HttpStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private final JwtService jwtService;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAll() {
        try {
            List<Product> products = productRepository.findAll();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get all products");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        try {
            Product product = productRepository.findById(id)
                                            .orElseThrow(() -> new Exception("Product not found with id: " + id));
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get product by id");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all/byUserId/{userId}")
    public ResponseEntity<List<Product>> getProductsByUserId(@PathVariable String userId) {
        try {
            List<Product> products = productRepository.findByUserId(userId);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get products by user id");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/createProduct")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productRequest,
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
            System.out.println("Failed to create product");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id,
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
            System.out.println("Failed to update product");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        try {
            productRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to delete product");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
