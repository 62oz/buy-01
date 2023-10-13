package buy01.userservice.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import buy01.userservice.enums.Role;
import buy01.userservice.exceptions.ResourceNotFoundException;
import buy01.userservice.models.User;
import buy01.userservice.models.ClientResponse.ClientResponseBuilder;
import buy01.userservice.models.ClientResponse;
import buy01.userservice.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class UserService {

    @Value("${admin.default.name}")
    private String adminName;

    @Value("${admin.default.email}")
    private String adminEmail;

    @Value("${admin.default.password}")
    private String adminPassword;

    @Value("${pruductServiceAccount.default.name}")
    private String productServiceAccountName;

    @Value("${pruductServiceAccount.default.email}")
    private String productServiceAccountEmail;

    @Value("${pruductServiceAccount.default.password}")
    private String productServiceAccountPassword;

    @Value("${mediaServiceAccount.default.name}")
    private String mediaServiceAccountName;

    @Value("${mediaServiceAccount.default.email}")
    private String mediaServiceAccountEmail;

    @Value("${mediaServiceAccount.default.password}")
    private String mediaServiceAccountPassword;

    @Value("${authServiceAccount.default.name}")
    private String authServiceAccountName;

    @Value("${authServiceAccount.default.email}")
    private String authServiceAccountEmail;

    @Value("${authServiceAccount.default.password}")
    private String authServiceAccountPassword;

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<ClientResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<ClientResponse> userResponses = users.stream()
                                               .map(this::mapToClientResponse)
                                               .collect(Collectors.toList());
        return userResponses;
    }

    public ClientResponse getUserById(String id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));
        return mapToClientResponse(user);
    }

    public ClientResponse getUserByName(String name) {
        User user = userRepository.findByName(name).orElseThrow();
        return mapToClientResponse(user);
    }

    public ClientResponse getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow();
        return mapToClientResponse(user);
    }

    public User updateUser(String id, User updatedUser, String authenticatedUserName) {
        userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));

        User authenticatedUser = userRepository.findByName(authenticatedUserName)
                                            .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found in database."));

        if (!authenticatedUser.getRole().equals(Role.ROLE_ADMIN) && updatedUser.getRole().equals(Role.ROLE_ADMIN)) {
            throw new AccessDeniedException("Only admins can update a user's role to admin.");
        }

        updatedUser.setId(id);
        return userRepository.save(updatedUser);
    }


    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    private ClientResponse mapToClientResponse(User user) {
        User authenticatedUser = getAuthenticatedUser();

        ClientResponseBuilder responseBuilder = ClientResponse.builder()
                                                                    .name(user.getUsername())
                                                                    .email(user.getEmail())
                                                                    .id("hidden");

        if (authenticatedUser != null) {
            boolean isAdmin = authenticatedUser.getRole().equals(Role.ROLE_ADMIN);
            boolean isSelf = user.getId().equals(authenticatedUser.getId());

            if (isAdmin || isSelf) {
                responseBuilder.id(user.getId());
            }
        }

        return responseBuilder.build();
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return null;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByName(userDetails.getUsername())
                            .orElse(null);
    }


    @PostConstruct
    public void initDefaultAdmin() {
        // Check if the default admin exists in the DB
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = User.builder()
                    .username(adminName)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }

    @PostConstruct
    public void initProductServiceAccount() {
        // Check if the product service account exists in the DB
        if (!userRepository.existsByEmail(productServiceAccountEmail)) {
            User productServiceAccount = User.builder()
                    .username(productServiceAccountName)
                    .email(productServiceAccountEmail)
                    .password(passwordEncoder.encode(productServiceAccountPassword))
                    .role(Role.ROLE_SERVICE)
                    .build();
            userRepository.save(productServiceAccount);
        }
    }

    @PostConstruct
    public void initMediaServiceAccount() {
        // Check if the media service account exists in the DB
        if (!userRepository.existsByEmail(mediaServiceAccountEmail)) {
            User mediaServiceAccount = User.builder()
                    .username(mediaServiceAccountName)
                    .email(mediaServiceAccountEmail)
                    .password(passwordEncoder.encode(mediaServiceAccountPassword))
                    .role(Role.ROLE_SERVICE)
                    .build();
            userRepository.save(mediaServiceAccount);
        }
    }

    @PostConstruct
    public void initAuthServiceAccount() {
        // Check if the media service account exists in the DB
        if (!userRepository.existsByEmail(authServiceAccountEmail)) {
            User mediaServiceAccount = User.builder()
                    .username(authServiceAccountName)
                    .email(authServiceAccountEmail)
                    .password(passwordEncoder.encode(authServiceAccountPassword))
                    .role(Role.ROLE_SERVICE)
                    .build();
            userRepository.save(mediaServiceAccount);
        }
    }
}
