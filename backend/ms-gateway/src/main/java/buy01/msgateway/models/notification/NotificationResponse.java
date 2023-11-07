package buy01.msgateway.models.notification;

import buy01.msgateway.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private String id;
    private String userId;
    private String message;
    private Status status;
    private String createdAt;
}
