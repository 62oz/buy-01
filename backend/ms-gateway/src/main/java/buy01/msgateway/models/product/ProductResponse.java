package buy01.msgateway.models.product;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    String id;
    String name;
    String description;
    BigDecimal price;
    Integer quantity;
    Integer availableQuantity = quantity;
    String userId;
}
