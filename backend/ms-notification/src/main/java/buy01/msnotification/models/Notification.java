package buy01.msnotification.models;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import buy01.msnotification.enums.Status;
import lombok.Data;

@Document
@Data
public class Notification {
    private String id;
    @NotNull
    @NotBlank
    private String userId;
    @NotNull(message = "Message cannot be null.")
    @NotBlank(message = "Message cannot be blank.")
    private String message;
    private Status status = Status.STATUS_UNREAD;
    private String createdAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date());
}
