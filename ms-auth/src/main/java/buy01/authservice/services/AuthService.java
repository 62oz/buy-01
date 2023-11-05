package buy01.authservice.services;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import buy01.authservice.models.AuthResponse;
import buy01.authservice.models.LoginRequest;
import buy01.authservice.models.RegisterRequest;
import buy01.authservice.enums.Role;
import buy01.authservice.exceptions.AuthenticationException;
import buy01.authservice.models.Account;
import buy01.authservice.repositories.AccountRepository;

public class AuthService {

    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final AccountRepository accountRepository;
    @Autowired
    private final JwtService jwtService;

    public AuthService(PasswordEncoder passwordEncoder, AccountRepository accountRepository, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.jwtService = jwtService;
    }

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

     private String generateRandomSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }
}
