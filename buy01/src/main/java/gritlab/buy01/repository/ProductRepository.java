package gritlab.buy01.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import gritlab.buy01.domain.Product;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByUserId(String userId);
}

