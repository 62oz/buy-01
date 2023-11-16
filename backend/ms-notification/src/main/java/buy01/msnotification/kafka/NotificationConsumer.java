package buy01.msnotification.kafka;

import org.springframework.kafka.annotation.KafkaListener;

import buy01.msnotification.models.ProductRequest;
import buy01.msnotification.services.NotificationService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "items-sold-notification", groupId = "notification-service")
    public void itemsSoldNotification(ProductRequest product) {
        notificationService.createNotification(product);
    }
}
