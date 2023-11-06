package buy01.productservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import buy01.productservice.models.Product;
import buy01.productservice.repositories.ProductRepository;

import org.springframework.http.ResponseEntity;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("api/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

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
}
