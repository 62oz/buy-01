package buy01.userservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import buy01.userservice.models.User;
import buy01.userservice.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
