package buy01.msorder.services;

import java.util.List;

import org.springframework.stereotype.Service;

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

    public void addItem(Order order, OrderItem orderItem) {
        List<OrderItem> itemsInOrder = order.getItems();
        for (OrderItem item : itemsInOrder) {
            if (item.getProductId().equals(orderItem.getProductId())) {
                item.setQuantity(item.getQuantity() + orderItem.getQuantity());
                orderRepository.save(order);
                return;
            }
        }
        itemsInOrder.add(orderItem);
        orderRepository.save(order);
        orderProducer.updateInventory(orderItem);
    }
}
