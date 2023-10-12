package buy01.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import buy01.authservice.domain.ClientAuthenticationRequest;
import buy01.authservice.domain.ClientAuthenticationResponse;
import buy01.authservice.domain.ClientRegistrationRequest;
import buy01.authservice.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("/")
    public ResponseEntity<String> greet() {
        return ResponseEntity.ok("Authentication Service is up!");
    }

    @PostMapping("/register")
    public ResponseEntity<ClientAuthenticationResponse> register(
            @Valid @RequestBody ClientRegistrationRequest request
    ) throws Exception {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ClientAuthenticationResponse> authenticate(
            @RequestBody ClientAuthenticationRequest request
    ) throws Exception {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}