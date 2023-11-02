package buy01.mediaservice.models;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Document
@Data
public class ClientMediaRequest {
    @NotNull(message = "Image path cannot be null")
    @NotBlank(message = "Image path cannot be blank")
    private String imagePath;
    private String productId;
}
