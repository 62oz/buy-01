package buy01.authservice.services;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import buy01.authservice.models.UserRegistrationRequest;

public class KafkaProducerService {
   @Autowired
    private KafkaTemplate<String, UserRegistrationRequest> kafkaTemplate;

    private final Map<String, CompletableFuture<Boolean>> responseFutures = new ConcurrentHashMap<>();

    public CompletableFuture<Boolean> sendUserRegistrationRequest(UserRegistrationRequest userDto) {
        String requestId = UUID.randomUUID().toString();
        userDto.setRequestId(requestId);

        CompletableFuture<Boolean> future = new CompletableFuture<>();
        responseFutures.put(requestId, future);

        kafkaTemplate.send("user-registration-request-topic", userDto);
        return future;
    }

    public void handleRegistrationResponse(String requestId, boolean isSuccessful) {
        CompletableFuture<Boolean> future = responseFutures.get(requestId);
        if (future != null) {
            future.complete(isSuccessful);
            responseFutures.remove(requestId);
        }
    }
}
