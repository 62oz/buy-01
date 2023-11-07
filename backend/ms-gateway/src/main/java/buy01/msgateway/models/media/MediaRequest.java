package buy01.msgateway.models.media;

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
