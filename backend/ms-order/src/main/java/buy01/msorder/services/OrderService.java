package buy01.msorder.services;

import java.math.BigDecimal;
import java.util.List;

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

    public void emptyOrder(Order order) {
        List<OrderItem> itemsInOrder = order.getItems();
        for (OrderItem item : itemsInOrder) {
            item.setQuantity(-item.getQuantity());
            orderProducer.updateInventory(item);
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.getItems().clear();
        order.setTotalAmount(null);
    }

    public void addItem(Order order, OrderItem orderItem) {
        List<OrderItem> itemsInOrder = order.getItems();
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
        order.getTotalAmount().add(quantityBD.multiply(orderItem.getUnitPrice()));
        orderRepository.save(order);
        orderProducer.updateInventory(orderItem);
    }

    public void removeItem(Order order, OrderItem orderItem) {
        List<OrderItem> itemsInOrder = order.getItems();
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
        order.getTotalAmount().subtract(quantityBD.multiply(orderItem.getUnitPrice()));
        orderRepository.save(order);
        orderItem.setQuantity(-orderItem.getQuantity());
        orderProducer.updateInventory(orderItem);
    }
}
