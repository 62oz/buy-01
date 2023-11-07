package buy01.msgateway.models.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaResponse {
    private String id;
    private String path;
    private String productId;
    private String userId;
}
