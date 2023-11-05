package buy01.msgateway.services;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import buy01.msgateway.models.auth.RegisterRequest;
import buy01.msgateway.models.user.UserResponse;
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

    public List<UserResponse> getAllUsers() {
        ResponseEntity<List<UserResponse>> response = restTemplate.getForEntity(
            "http://ms-user/api/user/all",
            null,
            new ParameterizedTypeReference<List<UserResponse>>() {});

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to get all users");
        }

        return response.getBody();
    }

    public UserResponse getUserById(String id) {
        ResponseEntity<UserResponse> response = restTemplate.getForEntity(
            "http://ms-user/api/user/" + id,
            null,
            UserResponse.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to get user by id");
        }

        return response.getBody();
    }

    public UserResponse getUserByName(String name) {
        ResponseEntity<UserResponse> response = restTemplate.getForEntity(
            "http://ms-user/api/user/byName/" + name,
            null,
            UserResponse.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to get user by name");
        }

        return response.getBody();
    }

    public UserResponse getUserByEmail(String email) {
        ResponseEntity<UserResponse> response = restTemplate.getForEntity(
            "http://ms-user/api/user/byEmail/" + email,
            null,
            UserResponse.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to get user by email");
        }

        return response.getBody();
    }

    public void updateUser(String id, RegisterRequest registerRequest) {
        ResponseEntity<Void> response = restTemplate.postForEntity(
            "http://ms-user/api/user/" + id,
            registerRequest,
            Void.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to update user");
        }
    }

    public void deleteUser(String id) {
        ResponseEntity<Void> response = restTemplate.getForEntity(
            "http://ms-user/api/user/delete/" + id,
            null,
            Void.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to delete user");
        }
    }
}
