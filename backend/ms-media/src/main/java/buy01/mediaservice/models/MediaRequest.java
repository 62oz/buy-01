package buy01.mediaservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaRequest {
    private String path;
    private String productId;
    private String userId;
}
