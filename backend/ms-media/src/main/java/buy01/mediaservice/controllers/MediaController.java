package buy01.mediaservice.controllers;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import buy01.mediaservice.models.Media;
import buy01.mediaservice.models.MediaRequest;
import buy01.mediaservice.repositories.MediaRepository;
import buy01.mediaservice.services.JwtService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/media")
public class MediaController {

    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private final JwtService jwtService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllMedia() {
        try {
            List<Media> media = mediaRepository.findAll();
            return ResponseEntity.ok(media);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get all media. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to get all media. Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMediaById(String id) {
        try {
            Media media = mediaRepository.findById(id)
                                            .orElseThrow(() -> new Exception("Media not found with id: " + id));
            return ResponseEntity.ok(media);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get media by id. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to get media by id. Error: " + e.getMessage());
        }
    }

    @GetMapping("/all/byProductId/{productId}")
    public ResponseEntity<?> getMediaByProductId(String productId) {
        try {
            List<Media> media = mediaRepository.findByProductId(productId);
            return ResponseEntity.ok(media);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to get media by product id. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to get media by product id. Error: " + e.getMessage());
        }
    }

    @PostMapping("/createMedia")
    public ResponseEntity<?> createMedia(@RequestBody MediaRequest mediaRequest,
                                            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String jwt = authorizationHeader.substring(7);
            // ADD error handling I guess
            String userId = jwtService.extractUserId(jwt);
            Media newMedia = new Media();
            newMedia.setUserId(userId);
            newMedia.setProductId(mediaRequest.getProductId());
            newMedia.setPath(mediaRequest.getPath());

            mediaRepository.save(newMedia);
            return ResponseEntity.ok(newMedia);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to create media. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to create media. Error: " + e.getMessage());
        }
    }

    @PutMapping("/updateMedia/{id}")
    public ResponseEntity<?> updateMedia(@PathVariable String id,
                                            @RequestBody MediaRequest mediaRequest) {
        try {
            Media media = mediaRepository.findById(id)
                                            .orElseThrow(() -> new Exception("Media not found with id: " + id));
            setIfNotNullOrEmptyString(media::setPath, mediaRequest.getPath());

            mediaRepository.save(media);
            return ResponseEntity.ok(media);
        } catch (Exception e) {
            // ADD LOGGING!!!
            System.out.println("Failed to update media. Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to update media. Error: " + e.getMessage());
        }
    }

    private void setIfNotNullOrEmptyString(Consumer<String> setter, String value) {
        if (value != null && !value.isEmpty()) {
            setter.accept(value);
        }
    }
}
