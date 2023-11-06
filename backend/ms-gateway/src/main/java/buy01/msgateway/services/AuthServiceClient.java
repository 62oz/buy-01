package buy01.msgateway.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import buy01.msgateway.exceptions.AuthenticationException;
import buy01.msgateway.models.auth.AuthResponse;
import buy01.msgateway.models.auth.LoginRequest;
import buy01.msgateway.models.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceClient {

    private final RestTemplate restTemplate;

    public AuthResponse authenticate(LoginRequest loginRequest) {
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
            "http://ms-auth/api/auth/login",
            loginRequest,
            AuthResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new AuthenticationException("Authentication failed.");
        }
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
            "http://ms-auth/api/auth/register",
            registerRequest,
            AuthResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new AuthenticationException("Registration failed.");
        }
    }

    public void accountExists(String username) {
        ResponseEntity<Boolean> response = restTemplate.getForEntity(
            "http://ms-auth/api/auth/account-exists/" + username,
            Boolean.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new AuthenticationException("User does not exist.");
        }
    }
}
