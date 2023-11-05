package buy01.msgateway.services;

@Service
public class UserServiceClient {
    private final RestTemplate restTemplate;

    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void createUser(String userId, RegisterRequest registerRequest) {
        ResponseEntity<Void> response = restTemplate.postForEntity(
            "http://ms-user/api/user/createProile/" + userId,
            registerRequest,
            Void.class);
    }

    if (response.getStatusCode() != HttpStatus.OK) {
        throw new RuntimeException("Failed to create user profile");
    }
}
