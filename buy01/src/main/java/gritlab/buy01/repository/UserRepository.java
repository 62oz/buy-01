package gritlab.buy01.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import gritlab.buy01.domain.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByName(String name);
    Optional<User> findUserByEmail(String email);

    @Query("{'productId.userId': ?0}")
    Optional<User> findByProductId(String userId);
    boolean existsByEmail(String adminEmail);
}
