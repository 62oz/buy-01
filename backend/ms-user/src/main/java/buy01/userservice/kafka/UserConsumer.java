package buy01.userservice.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import buy01.userservice.services.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserConsumer {

    @Autowired
    private final UserService userService;

    @KafkaListener(topics = "delete-account", groupId = "user-service")
    public void deleteUser(String accountId) {
        userService.deleteUser(accountId);
    }
}
