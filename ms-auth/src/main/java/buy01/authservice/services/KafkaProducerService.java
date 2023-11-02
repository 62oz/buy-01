package buy01.ms-auth.services;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import buy01.ms-auth.models.user.UserAuthenticationRequest;
import buy01.ms-auth.models.user.UserAuthenticationResponse;
import buy01.ms-auth.models.user.UserRegistrationRequest;

public class KafkaProducerService {

   @Autowired
    private KafkaTemplate<String, UserRegistrationRequest> kafkaRegTemplate;
    private final Map<String, CompletableFuture<String>> responseFutures = new ConcurrentHashMap<>();

    public CompletableFuture<String> sendUserRegistrationRequest(UserRegistrationRequest userDto) {
        String requestId = UUID.randomUUID().toString();
        userDto.setRequestId(requestId);

        CompletableFuture<String> future = new CompletableFuture<>();
        responseFutures.put(requestId, future);

        kafkaRegTemplate.send("user-registration-request-topic", userDto);
        return future;
    }

    public void handleRegistrationResponse(String requestId, String userId) {
        CompletableFuture<String> future = responseFutures.get(requestId);
        if (future != null) {
            future.complete(userId);
            responseFutures.remove(requestId);
        }
    }

    @Autowired
    private KafkaTemplate<String, UserAuthenticationRequest> kafkaAuthTemplate;
    private final Map<String, CompletableFuture<UserAuthenticationResponse>> pendingRequests = new ConcurrentHashMap<>();

    public UserAuthenticationResponse getUserByUsername(String username) throws UsernameNotFoundException {
        String requestId = UUID.randomUUID().toString();

        CompletableFuture<UserAuthenticationResponse> future = new CompletableFuture<>();
        pendingRequests.put(requestId, future);

        kafkaAuthTemplate.send("user-details-request-topic", new UserAuthenticationRequest(requestId, username));

        try {
            return future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new UsernameNotFoundException("Error fetching user details", e);
        } finally {
            pendingRequests.remove(requestId);
        }
    }

    public void handleAuthenticationResponse(String requestId, UserAuthenticationResponse response) {
        CompletableFuture<UserAuthenticationResponse> future = pendingRequests.get(requestId);
        if (future != null) {
            future.complete(response);
            pendingRequests.remove(requestId);
        }
    }
}
