package buy01.productservice.models;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Document
@Data
public class ClientProductRequest {
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    private String name;
    private String description;
    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
    private Double price;
    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0", inclusive = true, message = "Quantity must be non-negative")
    private Integer quantity;
}
