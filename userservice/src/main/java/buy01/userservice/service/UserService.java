package buy01.userservice.service;

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

import buy01.userservice.domain.User;
import buy01.userservice.domain.UserResponse;
import buy01.userservice.enums.Role;
import buy01.userservice.exception.ResourceNotFoundException;
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

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = users.stream()
                                               .map(this::mapToUserResponse)
                                               .collect(Collectors.toList());
        return userResponses;
    }

    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));
        return mapToUserResponse(user);
    }

    /* public UserResponse getUserByProductId(String productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        String userId = product.getUserId();
        User user = userRepository.findById(userId).orElseThrow();
        return mapToUserResponse(user);
    } */

    public UserResponse getUserByName(String name) {
        User user = userRepository.findByName(name).orElseThrow();
        return mapToUserResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow();
        return mapToUserResponse(user);
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

    private UserResponse mapToUserResponse(User user) {
        User authenticatedUser = getAuthenticatedUser();

        UserResponse.UserResponseBuilder responseBuilder = UserResponse.builder()
                                                                    .name(user.getName())
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
                    .name(adminName)
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
                    .name(productServiceAccountName)
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
                    .name(mediaServiceAccountName)
                    .email(mediaServiceAccountEmail)
                    .password(passwordEncoder.encode(mediaServiceAccountPassword))
                    .role(Role.ROLE_SERVICE)
                    .build();
            userRepository.save(mediaServiceAccount);
        }
    }
}
