package buy01.msorder.controllers;

import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import buy01.msorder.models.Order;
import buy01.msorder.models.OrderItem;
import buy01.msorder.repositories.OrderRepository;
import buy01.msorder.services.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @PostMapping("/create/{userId}")
    public void createOrder(@PathVariable String userId) {
        Optional<Order> order = orderRepository.findByUserId(userId);
        if (order.isPresent()) {
            throw new RuntimeException("Order already exists for user with id: " + userId);
        } else {
            Order newOrder = new Order();
            newOrder.setUserId(userId);
            orderRepository.save(newOrder);
        }
    }

    @PreAuthorize("hasRole(\"ROLE_ADMIN\") or #userId == authentication.principal.id")
    @PutMapping("/empty/{userId}")
    public void emptyOrder(@PathVariable String userId) {
        Optional<Order> orderOptional = orderRepository.findByUserId(userId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            orderService.emptyOrder(order);
        } else {
            throw new RuntimeException("Order does not exist for user with id: " + userId);
        }
    }

    @PreAuthorize("hasRole(\"ROLE_ADMIN\") or #userId == authentication.principal.id")
    @PutMapping("/add-item/{userId}")
    public void addItem(@PathVariable String userId, OrderItem orderItem) {
        Optional<Order> orderOptional = orderRepository.findByUserId(userId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            orderService.addItem(order, orderItem);
        } else {
            // This is supposed to never happen, because order is created when user is created
            throw new RuntimeException("Order does not exist for user with id: " + userId );
        }
    }
}
