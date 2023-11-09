package buy01.mediaservice.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import buy01.mediaservice.services.MediaService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MediaConsumer {

    @Autowired
    private final MediaService mediaService;

    @KafkaListener(topics = "delete-product", groupId = "media-service")
    public void deleteProductMedia(String productId) {
        mediaService.deleteProductMedia(productId);
    }
}
