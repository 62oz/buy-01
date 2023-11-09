package buy01.mediaservice.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;

import buy01.mediaservice.models.Media;

import java.util.List;

public interface MediaRepository extends MongoRepository<Media, String> {
    List<Media> findByUserId(String userId);
    List<Media> findByProductId(String productId);
    void deleteByProductId(String productId);
}

