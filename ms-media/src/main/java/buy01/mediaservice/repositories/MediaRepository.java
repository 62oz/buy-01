package buy01.ms-media.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;

import buy01.ms-media.models.Media;

import java.util.List;

public interface MediaRepository extends MongoRepository<Media, String> {
    List<Media> findByUserId(String userId);
    List<Media> findByProductId(String productId);
}

