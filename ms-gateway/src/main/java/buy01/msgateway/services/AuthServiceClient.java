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

    public void validateToken(String token) {
        ResponseEntity<Void> response = restTemplate.postForEntity(
            "http://ms-auth/api/auth/validateToken",
            token,
            Boolean.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new AuthenticationException("Invalid token.");
        }
    }
}
