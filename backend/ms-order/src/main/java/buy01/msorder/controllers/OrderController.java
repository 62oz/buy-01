package buy01.msorder.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import buy01.msorder.models.Order;
import buy01.msorder.models.OrderItem;
import buy01.msorder.services.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<?> createOrder(@PathVariable String userId) {
        Order newOrder = orderService.createOrder(userId);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole(\"ROLE_ADMIN\") or #userId == authentication.principal.id")
    @PutMapping("/empty/{userId}")
    public ResponseEntity<?> emptyOrder(@PathVariable String userId) {
        Order emptyOrder = orderService.emptyOrder(userId);
        return new ResponseEntity<>(emptyOrder, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(\"ROLE_ADMIN\") or #userId == authentication.principal.id")
    @PutMapping("/add-item/{userId}")
    public ResponseEntity<?> addItem(@PathVariable String userId, OrderItem orderItem) {
        Order updatedOrder = orderService.addItem(userId, orderItem);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(\"ROLE_ADMIN\") or #userId == authentication.principal.id")
    @PutMapping("/remove-item/{userId}")
    public ResponseEntity<?> removeItem(@PathVariable String userId, OrderItem orderItem) {
        Order updatedOrder = orderService.removeItem(userId, orderItem);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }
}
