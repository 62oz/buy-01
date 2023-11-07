package buy01.msgateway.services;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceClient {

    private final RestTemplate restTemplate;

    public void createOrder(String userId) {
        ResponseEntity<Void> response = restTemplate.postForEntity(
            "http://ms-order/api/order/create/" + userId,
            null,
            Void.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to create order");
        }
    }

    public void emptyOrder(String userId) {
        ResponseEntity<Void> response = restTemplate.exchange(
            "http://ms-order/api/order/empty/" + userId,
            HttpMethod.PUT,
            null,
            Void.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to empty order");
        }
    }
}
