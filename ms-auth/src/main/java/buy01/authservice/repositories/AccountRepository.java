package buy01.authservice.repositories;

import buy01.authservice.models.Account;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account, String> {

    Optional<Account> findByUsername(String username);
    Optional<Account> findUserByEmail(String email);

}
