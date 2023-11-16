package buy01.msnotification.services;

import org.springframework.stereotype.Service;

import buy01.msnotification.models.Notification;
import buy01.msnotification.models.ProductRequest;
import buy01.msnotification.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Boolean isOwner(String notificationId, String authenticatedId) {
        return notificationRepository.findById(notificationId)
                .map(notification -> notification.getUserId().equals(authenticatedId))
                .orElse(false);
    }

    public void createNotification(ProductRequest product) {
        Notification notification = new Notification();
        notification.setUserId(product.getUserId());
        notification.setMessage(product.getQuantity() + " items of product " + product.getName() + " were sold.");
        notificationRepository.save(notification);
    }
}
