package buy01.authservice.services;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import buy01.authservice.models.UserAuthenticationRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaUserDetailsService implements UserDetailsService {

    private final KafkaTemplate<String, UserAuthenticationRequest> kafkaTemplate;
    private final Map<String, CompletableFuture<UserDetails>> pendingRequests = new ConcurrentHashMap<>();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Create an ID for this request to correlate request and response
        String requestId = UUID.randomUUID().toString();

        // Create a future that will be completed when the response is received
        CompletableFuture<UserDetails> future = new CompletableFuture<>();
        pendingRequests.put(requestId, future);

        // Produce the request to Kafka
        kafkaTemplate.send("user-details-request-topic", new UserAuthenticationRequest(requestId, username));

        try {
            // Wait for the response (with a timeout)
            return future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new UsernameNotFoundException("Error fetching user details", e);
        } finally {
            pendingRequests.remove(requestId);
        }
    }

    // This method is called when the response is received from Kafka
    public void completeUserDetailsRequest(String requestId, UserDetails userDetails) {
        CompletableFuture<UserDetails> future = pendingRequests.get(requestId);
        if (future != null) {
            future.complete(userDetails);
        }
    }
}
