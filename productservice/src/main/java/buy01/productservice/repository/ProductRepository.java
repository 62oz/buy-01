package buy01.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import buy01.productservice.domain.Product;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByUserId(String userId);
}

