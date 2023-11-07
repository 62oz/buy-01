package buy01.msorder.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import buy01.msorder.enums.OrderStatus;
import buy01.msorder.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Document
@Data
public class Order {
    @Id
    private String id;
    @NotNull
    private String userId;
    @NotNull
    private List<OrderItem> items;
    @NotNull
    private OrderStatus status;
    @NotNull
    private PaymentMethod paymentMethod;
    @NotNull
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
}
