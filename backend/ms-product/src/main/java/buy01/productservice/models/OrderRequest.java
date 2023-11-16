package buy01.productservice.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import buy01.productservice.enums.OrderStatus;
import buy01.productservice.enums.PaymentMethod;
import lombok.Data;

@Data
public class OrderRequest {
    private String id;
    private String userId;
    private List<OrderItemRequest> items;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
}
