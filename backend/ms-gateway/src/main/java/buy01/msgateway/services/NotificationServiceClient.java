package buy01.msgateway.services;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import buy01.msgateway.models.notification.NotificationResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceClient {

    private final RestTemplate restTemplate;

    public List<NotificationResponse> getAllNotifications() {
        ResponseEntity<List<NotificationResponse>> response = restTemplate.getForEntity(
            "http://ms-notification/api/notification/all",
            null,
            new ParameterizedTypeReference<List<NotificationResponse>>() {});

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to get all notifications");
        }

        return response.getBody();
    }

    public NotificationResponse getNotificationById(String id) {
        ResponseEntity<NotificationResponse> response = restTemplate.getForEntity(
            "http://ms-notification/api/notification/" + id,
            null,
            NotificationResponse.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to get notification by id");
        }

        return response.getBody();
    }

    public List<NotificationResponse> getNotificationByUserId(String userId) {
        ResponseEntity<List<NotificationResponse>> response = restTemplate.getForEntity(
            "http://ms-notification/api/notification/all/byUserId/" + userId,
            null,
            new ParameterizedTypeReference<List<NotificationResponse>>() {});

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to get notification by user id");
        }

        return response.getBody();
    }

    public void markNotificationAsRead(String id) {
        try {
            restTemplate.put("http://ms-notification/api/notification/markAsRead/" + id, null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to mark notification " + id + " as read.");
        }
    }

    public void deleteNotification(String id) {
        try {
            restTemplate.delete("http://ms-notification/api/notification/deleteNotification/" + id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete notification " + id);
        }
    }
}
