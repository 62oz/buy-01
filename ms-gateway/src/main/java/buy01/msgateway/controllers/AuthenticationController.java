package buy01.msgateway.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import buy01.msgateway.exceptions.AuthenticationException;
import buy01.msgateway.models.AuthResponse;
import buy01.msgateway.models.LoginRequest;
import buy01.msgateway.models.RegisterRequest;
import buy01.msgateway.services.AuthServiceClient;
import buy01.msgateway.services.JwtService;
import buy01.msgateway.services.SecurityContextService;
import buy01.msgateway.services.UserServiceClient;

import java.util.Map;
import java.util.HashMap;

@RestController
public class AuthenticationController {

    private final AuthServiceClient authServiceClient;
    private final SecurityContextService securityContextService;
    private final JwtService jwtService;

    public AuthenticationController(JwtService jwtService,
                                    SecurityContextService securityContextService,
                                    AuthServiceClient authServiceClient,
                                    UserServiceClient userServiceClient)
    {
        this.jwtService = jwtService;
        this.securityContextService = securityContextService;
        this.authServiceClient = authServiceClient;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, UserServiceClient userServiceClient) {
        try {
            AuthResponse jwtDto = authServiceClient.authenticate(loginRequest);
            String jwt = jwtDto.getJwt();

            securityContextService.setAuthentication(jwt);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("jwt", jwt);

            return ResponseEntity.ok(responseBody);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest, UserServiceClient userServiceClient) {
        try {
            AuthResponse jwtDto = authServiceClient.register(registerRequest);
            String jwt = jwtDto.getJwt();
            String userId = jwtService.extractUserId(jwt);

            userServiceClient.createUser(userId, registerRequest);

            securityContextService.setAuthentication(jwt);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("jwt", jwt);

            return ResponseEntity.ok(responseBody);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Registration failed");
        }
    }
}
