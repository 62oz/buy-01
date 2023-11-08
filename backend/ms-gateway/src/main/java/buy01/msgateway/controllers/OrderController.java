package buy01.msgateway.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import buy01.msgateway.models.order.OrderItemRequest;
import buy01.msgateway.services.OrderServiceClient;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderServiceClient orderServiceClient;

    @PreAuthorize("hasRole(\"ROLE_ADMIN\") or hasRole(\"ROLE_CLIENT\")")
    @PutMapping("/empty/{userId}")
    public ResponseEntity<?> emptyOrder(@PathVariable String userId) {
        orderServiceClient.emptyOrder(userId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole(\"ROLE_ADMIN\") or hasRole(\"ROLE_CLIENT\")")
    @PutMapping("/add-items/{userId}")
    // The hope here is that the orderItemRequest object carries:
    // productId, quantity, and unitPrice of the product
    public ResponseEntity<?> addItem(@PathVariable String userId, @RequestBody OrderItemRequest orderItem) {
        orderServiceClient.addItem(userId, orderItem);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole(\"ROLE_ADMIN\") or hasRole(\"ROLE_CLIENT\")")
    @PutMapping("/remove-items/{userId}")
    // The hope here is that the orderItemRequest object carries:
    // productId, quantity, and unitPrice of the product
    public ResponseEntity<?> removeItem(@PathVariable String userId, @RequestBody OrderItemRequest orderItem) {
        orderServiceClient.removeItem(userId, orderItem);
        return ResponseEntity.ok().build();
    }
}
