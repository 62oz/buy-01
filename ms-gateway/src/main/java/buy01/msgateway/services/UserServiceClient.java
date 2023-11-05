package buy01.msgateway.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import buy01.msgateway.models.RegisterRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceClient {
    private final RestTemplate restTemplate;

    public void createUser(String userId, RegisterRequest registerRequest) {
        ResponseEntity<Void> response = restTemplate.postForEntity(
            "http://ms-user/api/user/createProfile/" + userId,
            registerRequest,
            Void.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to create user profile");
        }
    }
}
