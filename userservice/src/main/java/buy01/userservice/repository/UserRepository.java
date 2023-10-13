package buy01.userservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import buy01.userservice.models.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByName(String username);
    Optional<User> findUserByEmail(String email);

    @Query("{'productId.userId': ?0}")
    Optional<User> findByProductId(String userId);
    boolean existsByEmail(String adminEmail);
}
