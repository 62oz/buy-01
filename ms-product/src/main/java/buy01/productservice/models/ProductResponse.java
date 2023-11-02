package buy01.ms-product.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    String id;
    String name;
    String description;
    Double price;
    Integer quantity;
    String userId;
}