package buy01.msnotification.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import buy01.msnotification.enums.NotificationStatus;
import buy01.msnotification.models.Notification;
import buy01.msnotification.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping("/all")
    public ResponseEntity<?> getAllNotifications() {
        try {
            List<Notification> notifications = notificationRepository.findAll();
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get all notifications. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to get all notifications. Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationById(String id) {
        try {
            Notification notification = notificationRepository.findById(id)
                                            .orElseThrow(() -> new Exception("Notification not found with id: " + id));
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get notification by id. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to get notification by id. Error: " + e.getMessage());
        }
    }

    @GetMapping("/all/byUserId/{userId}")
    public ResponseEntity<?> getNotificationByUserId(String userId) {
        try {
            List<Notification> notifications = notificationRepository.findByUserId(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get notification by user id. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to get notification by user id. Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\") or @notificationService.isOwner(#id, principal.id)")
    @PutMapping("/markAsRead/{id}")
    public ResponseEntity<?> markNotificationAsRead(String id) {
        try {
            Notification notification = notificationRepository.findById(id)
                                            .orElseThrow(() -> new Exception("Notification not found with id: " + id));
            notification.setStatus(NotificationStatus.READ);
            notificationRepository.save(notification);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to mark notification as read. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to mark notification as read. Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteNotification/{id}")
    public ResponseEntity<?> deleteNotification(String id) {
        try {
            notificationRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to delete notification. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to delete notification. Error: " + e.getMessage());
        }
    }
}
