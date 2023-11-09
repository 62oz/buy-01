package buy01.msorder.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import buy01.msorder.enums.OrderStatus;
import buy01.msorder.kafka.OrderProducer;
import buy01.msorder.models.Order;
import buy01.msorder.models.OrderItem;
import buy01.msorder.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer;

    public Order createOrder(String userId) {
        if (getPending(userId) != null) {
            throw new RuntimeException("There is already a pending order for user with id: " + userId);
        }
        Order newOrder = new Order();
        newOrder.setUserId(userId);
        orderRepository.save(newOrder);
        return newOrder;
    }

    private Order getPending(String userId) {
        List<Optional<Order>> orderOptionalList = orderRepository.findByUserId(userId);
        for (Optional<Order> orderOptional : orderOptionalList) {
            Order order = orderOptional.get();
            if (order.getStatus().equals(OrderStatus.PENDING)) {
                return order;
            }
        }
        return null;
    }

    public Order emptyOrder(String userId) {
        Order pendingOrder = getPending(userId);
        if (pendingOrder == null) {
            throw new RuntimeException("There is no pending order for user with id: " + userId);
        }
        pendingOrder.getItems().clear();
        pendingOrder.setTotalAmount(BigDecimal.ZERO);
        orderRepository.save(pendingOrder);
        return pendingOrder;
    }

    public Order addItem(String userId, OrderItem orderItem) {
        Order pendingOrder = getPending(userId);
        if (pendingOrder == null) {
            pendingOrder = createOrder(userId);
        }

        List<OrderItem> itemsInOrder = pendingOrder.getItems();
        Boolean itemExists = false;

        for (OrderItem item : itemsInOrder) {
            itemExists = item.getProductId().equals(orderItem.getProductId());
            if (itemExists) {
                item.setQuantity(item.getQuantity() + orderItem.getQuantity());
                break;
            }
        }

        if (!itemExists) {
            itemsInOrder.add(orderItem);
        }
        BigDecimal quantityBD = BigDecimal.valueOf(orderItem.getQuantity());
        pendingOrder.getTotalAmount().add(quantityBD.multiply(orderItem.getUnitPrice()));
        orderRepository.save(pendingOrder);
        orderProducer.updateInventory(orderItem);
        return pendingOrder;
    }

    public Order removeItem(String userId, OrderItem orderItem) {
        Order pendingOrder = getPending(userId);
        if (pendingOrder == null) {
            throw new RuntimeException("There is no pending order for user with id: " + userId);
        }

        List<OrderItem> itemsInOrder = pendingOrder.getItems();
        Boolean itemExists = false;

        for (OrderItem item : itemsInOrder) {
            itemExists = item.getProductId().equals(orderItem.getProductId());
            if (itemExists) {
                item.setQuantity(item.getQuantity() - orderItem.getQuantity());
                if (item.getQuantity() <= 0) {
                    itemsInOrder.remove(item);
                }
                break;
            }
        }

        if (!itemExists) {
            throw new RuntimeException("Item does not exist in order");
        }

        BigDecimal quantityBD = BigDecimal.valueOf(orderItem.getQuantity());
        pendingOrder.getTotalAmount().subtract(quantityBD.multiply(orderItem.getUnitPrice()));
        orderRepository.save(pendingOrder);
        orderItem.setQuantity(-orderItem.getQuantity());
        orderProducer.updateInventory(orderItem);
        return pendingOrder;
    }
}
