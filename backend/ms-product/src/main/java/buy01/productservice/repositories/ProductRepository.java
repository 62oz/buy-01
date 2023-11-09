package buy01.productservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import buy01.productservice.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Optional<Product>> findByUserId(String userId);
    void deleteByUserId(String userId);
}

