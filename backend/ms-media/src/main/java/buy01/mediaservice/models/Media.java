package buy01.mediaservice.models;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import org.springframework.data.annotation.Id;

@Document
@Data
public class Media {
    @Id
    private String id;
    @NotNull(message = "Path cannot be null")
    @NotBlank(message = "Path cannot be blank")
    private String path;
    private String productId;
    private String userId;
}
