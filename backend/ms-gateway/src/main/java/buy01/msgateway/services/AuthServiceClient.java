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

    public void editRole(String id, String role) {
        ResponseEntity<Void> response = restTemplate.postForEntity(
            "http://ms-auth/api/auth/edit-role" + id,
            role,
            Void.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new AuthenticationException("Role edit failed.");
        }
    }

    public void deleteAccount(String id) {
        ResponseEntity<Void> responseMedia = restTemplate.postForEntity(
            "http://ms-media/api/media/delete-account-media" + id,
            null,
            Void.class);

        if (responseMedia.getStatusCode() != HttpStatus.OK) {
            throw new AuthenticationException("Account deletion failed. Can't delete account media.");
        }

        ResponseEntity<Void> responseProduct = restTemplate.postForEntity(
            "http://ms-product/api/product/delete-account-products" + id,
            null,
            Void.class);

        if (responseProduct.getStatusCode() != HttpStatus.OK) {
            throw new AuthenticationException("Account deletion failed. Can't delete account products.");
        }

        ResponseEntity<Void> responseNotification = restTemplate.postForEntity(
            "http://ms-notification/api/notification/delete-account-notifications" + id,
            null,
            Void.class);

        if (responseNotification.getStatusCode() != HttpStatus.OK) {
            throw new AuthenticationException("Account deletion failed. Can't delete account notifications.");
        }

        ResponseEntity<Void> responseUser = restTemplate.postForEntity(
            "http://ms-user/api/user/delete-account-user" + id,
            null,
            Void.class);

        if (responseUser.getStatusCode() != HttpStatus.OK) {
            throw new AuthenticationException("Account deletion failed. Can't delete account user.");
        }

        ResponseEntity<Void> responseAccount = restTemplate.postForEntity(
            "http://ms-auth/api/auth/delete-account" + id,
            null,
            Void.class);

        if (responseAccount.getStatusCode() != HttpStatus.OK) {
            throw new AuthenticationException("Account deletion failed.");
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
