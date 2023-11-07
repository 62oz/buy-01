package buy01.msnotification.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import buy01.msnotification.models.Notification;


public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserId(String userId);
}
