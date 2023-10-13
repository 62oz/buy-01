package buy01.authservice.services;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import buy01.authservice.config.JwtService;
import buy01.authservice.enums.Role;
import buy01.authservice.exceptions.CustomAuthenticationException;
import buy01.authservice.exceptions.DuplicateUserException;
import buy01.authservice.models.ClientAuthenticationRequest;
import buy01.authservice.models.ClientAuthenticationResponse;
import buy01.authservice.models.ClientRegistrationRequest;
import buy01.authservice.models.UserAuthenticationResponse;
import buy01.authservice.models.UserRegistrationRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final KafkaProducerService kafkaProducerService;
    private final KafkaUserDetailsService kafkaUserDetailsService;

    public ClientAuthenticationResponse register(ClientRegistrationRequest request) throws Exception {
        String salt = generateRandomSalt();
        String saltedPassword = salt + request.getPassword();
        String backgroundColourHex = String.format("#%06x", new SecureRandom().nextInt(0xffffff + 1));
        String textColourHex = String.format("#%06x", new SecureRandom().nextInt(0xffffff + 1));
        String nameFormatted = request.getUsername().replaceAll("\\s+", "+");
        var userDto = UserRegistrationRequest.builder()
                .name(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(saltedPassword))
                .salt(salt)
                .role(Role.ROLE_CLIENT)
                .avatar("https://ui-avatars.com/api/?name=" + nameFormatted
                                                            + "&background=" + backgroundColourHex
                                                            + "&color=" + textColourHex)
                .build();

        // Send registration request to user service via Kafka
        CompletableFuture<Boolean> registrationFuture = kafkaProducerService.sendUserRegistrationRequest(userDto);
        boolean registrationSuccessful = registrationFuture.get();

        if (!registrationSuccessful) {
            throw new DuplicateUserException("Registration failed. User might already exist.");
        }

        var jwt = jwtService.generateToken(userDto);
        return ClientAuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    private String generateRandomSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    public ClientAuthenticationResponse authenticate(ClientAuthenticationRequest request) throws Exception {
        UserDetails userDetails = kafkaUserDetailsService.loadUserByUsername(request.getUsername());

        if (!(userDetails instanceof UserAuthenticationResponse)) {
            throw new CustomAuthenticationException("Bad credentials");
        }

        UserAuthenticationResponse userDto = (UserAuthenticationResponse) userDetails;

        String salt = userDto.getSalt();
        if (salt == null) {
            salt = "";
        }

        String saltedPassword = salt + request.getPassword();

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    userDto.getUsername(),
                    saltedPassword
                )
            );
        } catch (BadCredentialsException ex) {
            throw new CustomAuthenticationException("Bad credentials");
        }

        var jwt = jwtService.generateToken(userDetails);
        return ClientAuthenticationResponse.builder()
                .token(jwt)
                .build();
}

}
