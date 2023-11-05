package buy01.msgateway.controllers;

@RestController
public class AuthenticationController {

    private final AuthServiceClient authServiceClient;
    private final UserServiceClient userServiceClient;
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
        this.userServiceClient = userServiceClient;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, UserServiceClient userServiceClient) {
        try {
            AuthResponse jwtDto = authServiceClient.authenticate(loginRequest);
            String jwt = jwtDto.getJwt();

            securityContextService.setAuthentication(jwt);

            Map<Stirng, Object> responseBody = new HashMap<>();
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

            Map<Stirng, Object> responseBody = new HashMap<>();
            responseBody.put("jwt", jwt);

            return ResponseEntity.ok(responseBody);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Registration failed");
        }
    }
}
