package buy01.authservice.auth;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import buy01.authservice.config.JwtService;
import buy01.authservice.domain.User;
import buy01.authservice.enums.Role;
import buy01.authservice.exception.CustomAuthenticationException;
import buy01.authservice.exception.DuplicateUserException;
import buy01.authservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) throws DuplicateUserException {
        String salt = generateRandomSalt();
        String saltedPassword = salt + request.getPassword();
        String backgroundColourHex = String.format("#%06x", new SecureRandom().nextInt(0xffffff + 1));
        String textColourHex = String.format("#%06x", new SecureRandom().nextInt(0xffffff + 1));
        String nameFormatted = request.getName().replaceAll("\\s+", "+");
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(saltedPassword))
                .salt(salt)
                .role(Role.ROLE_CLIENT)
                .avatar("https://ui-avatars.com/api/?name=" + nameFormatted
                                                            + "&background=" + backgroundColourHex
                                                            + "&color=" + textColourHex)
                .build();

        try {
            userRepository.save(user);
        } catch (DuplicateKeyException ex) {
            if (ex.getMessage().contains("name")) {
                throw new DuplicateUserException("This name is already registered.");
            } else if (ex.getMessage().contains("email")) {
                throw new DuplicateUserException("This email is already registered.");
            }
            // Handle other duplicates or just throw a generic message
            throw new DuplicateUserException("Data conflict occurred. Please try again.");
        }

        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    private String generateRandomSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByName(request.getName())
            .orElseGet(() -> userRepository.findUserByEmail(request.getName())
            .orElseThrow(() -> new CustomAuthenticationException("Bad credentials")));

        String salt = user.getSalt();
        if (salt == null) {
            salt = "";
        }

        String saltedPassword = salt + request.getPassword();

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    user.getName(),
                    saltedPassword
                )
            );
        } catch (BadCredentialsException ex) {
            throw new CustomAuthenticationException("Bad credentials");
        }

        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }
}
