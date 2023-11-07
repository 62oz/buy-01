package buy01.msgateway.models.product;

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
    Double price;
    Integer quantity;
    Integer availableQuantity = quantity;
    String userId;
}
