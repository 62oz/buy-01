package buy01.ms-product.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import buy01.ms-product.models.Product;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByUserId(String userId);
}

