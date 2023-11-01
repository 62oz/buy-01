package buy01.productservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import buy01.productservice.domains.Product;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByUserId(String userId);
}

