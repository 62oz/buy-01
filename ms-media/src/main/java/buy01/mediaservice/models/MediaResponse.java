package buy01.ms-media.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaResponse {
    String id;
    String imagePath;
    String productId;
    String userId;
}
