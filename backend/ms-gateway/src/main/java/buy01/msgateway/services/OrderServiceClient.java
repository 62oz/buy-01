package buy01.msgateway.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import buy01.msgateway.models.order.OrderItemRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceClient {

    private final RestTemplate restTemplate;

    public ResponseEntity<?> getMyOrders(String userId) {
        ResponseEntity<?> response = restTemplate.getForEntity(
            "http://ms-order/api/order/my-orders/" + userId,
            Void.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to get orders");
        }

        return response;
    }

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

    public void addItem(String userId, OrderItemRequest orderItem) {
        HttpEntity<OrderItemRequest> request = new HttpEntity<>(orderItem);
        // Check if product available in this quantity (this is fully synchronous)
        ResponseEntity<Void> responseProduct = restTemplate.exchange(
            "http://ms-product/api/product/check-availabiliy",
            HttpMethod.GET,
            request,
            Void.class);

        if (responseProduct.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Item no longer available.");
        }

        // Add item to order synchronously (it will asynchronously update avialble quantity of product)
        ResponseEntity<Void> responseOrder = restTemplate.exchange(
            "http://ms-order/api/order/add-item/" + userId,
            HttpMethod.PUT,
            request,
            Void.class);

        if (responseOrder.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to add item to order.");
        }
    }

    public void removeItem(String userId, OrderItemRequest orderItem) {
        HttpEntity<OrderItemRequest> request = new HttpEntity<>(orderItem);
        // Remove item from order synchronously (it will asynchronously update avialble quantity of product)
        ResponseEntity<Void> responseOrder = restTemplate.exchange(
            "http://ms-order/api/order/remove-item/" + userId,
            HttpMethod.PUT,
            request,
            Void.class);

        if (responseOrder.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to remove item from order.");
        }
    }

    public ResponseEntity<?> checkout(String userId) {
        ResponseEntity<?> response = restTemplate.exchange(
            "http://ms-order/api/order/checkout/" + userId,
            HttpMethod.PUT,
            null,
            Void.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to checkout order");
        }

        return response;
    }
}
