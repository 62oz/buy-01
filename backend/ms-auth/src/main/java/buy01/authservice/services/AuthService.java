package buy01.authservice.services;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import buy01.authservice.models.AuthResponse;
import buy01.authservice.models.LoginRequest;
import buy01.authservice.models.RegisterRequest;
import buy01.authservice.enums.Role;
import buy01.authservice.exceptions.AuthenticationException;
import buy01.authservice.kafka.AuthProducer;
import buy01.authservice.models.Account;
import buy01.authservice.repositories.AccountRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthService {
    @Value("${admin.default.name}")
    private String adminName;

    @Value("${admin.default.email}")
    private String adminEmail;

    @Value("${admin.default.password}")
    private String adminPassword;

    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final AccountRepository accountRepository;
    @Autowired
    private final JwtService jwtService;
    private final AuthProducer authProducer;

    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Optional<Account> accountOptional = accountRepository.findByUsername(loginRequest.getUsername());
        if (accountOptional.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), accountOptional.get().getPassword())) {
            Account account = accountOptional.get();
            String token = jwtService.generateToken(account);
            account.setToken(token);
            accountRepository.save(account);
            return new AuthResponse(token);
        } else {
            throw new AuthenticationException("Invalid username or password.");
        }
    }

    public AuthResponse registerUser(RegisterRequest registerRequest) {
        Optional<Account> accountOptional = accountRepository.findByUsername(registerRequest.getUsername());
        if (!accountOptional.isPresent()) {
            accountOptional = accountRepository.findByEmail(registerRequest.getEmail());
            if (accountOptional.isPresent()) {
                throw new AuthenticationException("Email already exists.");
            } else {
                Account account = new Account();
                account.setUsername(registerRequest.getUsername());
                account.setEmail(registerRequest.getEmail());
                account.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
                account.setSalt(generateRandomSalt());
                account.setRole(Role.ROLE_CLIENT);
                String token = jwtService.generateToken(account);
                account.setToken(token);
                accountRepository.save(account);
                return new AuthResponse(token);
            }
        } else {
            throw new AuthenticationException("Username already exists.");
        }
    }

    public void editRole(String userId, String role, String jwt) {
        Role roleEnum = Role.valueOf(role);
        String authenticatedUserId = jwtService.extractUserId(jwt);
        Optional<Account> authenticatedAccountOptional = accountRepository.findByUserId(authenticatedUserId);

        if (authenticatedAccountOptional.isPresent()) {
            Account authenticatedAccount = authenticatedAccountOptional.get();
            Optional<Account> accountToEditOptional = accountRepository.findByUserId(userId);

            if (accountToEditOptional.isPresent()) {
                Account accountToEdit = accountToEditOptional.get();
                if (roleEnum.equals(Role.ROLE_ADMIN) && !authenticatedAccount.getRole().equals(Role.ROLE_ADMIN)) {
                    throw new AuthenticationException("Only admins can edit roles to admin.");
                } else {
                    accountToEdit.setRole(roleEnum);
                    accountRepository.save(accountToEdit);
                }
            } else {
                throw new AuthenticationException("Account to edit does not exist.");
            }

        } else {
            throw new AuthenticationException("Authenticated account does not exist. Check your token.");
        }
    }

    public void deleteAccount(String userId) {
        Optional<Account> accountOptional = accountRepository.findByUserId(userId);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            // Send kafka message to delete associated user profile and products
            authProducer.deleteAccount(account.getId());
            // Delete account
            accountRepository.delete(account);
        } else {
            throw new AuthenticationException("Account does not exist for user ID:" + userId);
        }
    }

    public void accountExists(String username) {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (!accountOptional.isPresent()) {
            throw new AuthenticationException("Account does not exist.");
        }
    }

     private String generateRandomSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    @PostConstruct
    public void initDefaultAdmin() {
        // Check if the default admin exists in the DB
        if (!accountRepository.existsByEmail(adminEmail)) {
            Account admin = Account.builder()
                    .username(adminName)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ROLE_ADMIN)
                    .build();
            accountRepository.save(admin);
        }
    }
}
