package buy01.userservice.services;

import org.springframework.stereotype.Service;

import buy01.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void deleteUser(String accountId) {
        userRepository.deleteById(accountId);
    }
}
