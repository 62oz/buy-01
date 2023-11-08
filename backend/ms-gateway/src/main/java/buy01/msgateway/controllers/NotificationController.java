package buy01.msgateway.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import buy01.msgateway.models.notification.NotificationResponse;
import buy01.msgateway.services.NotificationServiceClient;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationServiceClient notificationServiceClient;

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\")")
    @GetMapping("/all")
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        return ResponseEntity.ok(notificationServiceClient.getAllNotifications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotificationById(String id) {
        return ResponseEntity.ok(notificationServiceClient.getNotificationById(id));
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\") or #userId == principal.id")
    @GetMapping("/all/byUserId/{userId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationByUserId(String userId) {
        return ResponseEntity.ok(notificationServiceClient.getNotificationByUserId(userId));
    }

    @PutMapping("/markAsRead/{id}")
    public ResponseEntity<Void> markNotificationAsRead(String id) {
        notificationServiceClient.markNotificationAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\")")
    @DeleteMapping("/deleteNotification/{id}")
    public ResponseEntity<Void> deleteNotification(String id) {
        notificationServiceClient.deleteNotification(id);
        return ResponseEntity.ok().build();
    }
}
