package buy01.msgateway.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import buy01.msgateway.exceptions.AuthenticationException;
import buy01.msgateway.models.AuthResponse;
import buy01.msgateway.models.LoginRequest;
import buy01.msgateway.models.RegisterRequest;

@Service
public class AuthServiceClient {

    private final RestTemplate restTemplate;

    public AuthServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

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

    public String validateToken(String token) {
        ResponseEntity<String> response = restTemplate.postForEntity(
            "http://ms-auth/api/auth/validateToken",
            token,
            String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new AuthenticationException("Token validation failed.");
        }
    }
}
