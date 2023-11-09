package buy01.msorder.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import buy01.msorder.models.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Optional<Order>> findByUserId(String userId);
}
