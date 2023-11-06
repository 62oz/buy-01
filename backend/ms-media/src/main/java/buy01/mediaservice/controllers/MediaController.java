package buy01.mediaservice.controllers;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import buy01.mediaservice.models.Media;
import buy01.mediaservice.models.MediaResponse;
import buy01.mediaservice.services.MediaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @GetMapping("/")
    public List<MediaResponse> getAll() {
        return mediaService.getAllMedia();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaResponse> getById(@PathVariable String id) {
        MediaResponse mediaResponse = mediaService.getMediaById(id);
        return ResponseEntity.ok(mediaResponse);
    }

    @GetMapping("/byUserId/{userId}")
    public List<MediaResponse> getByUserId(@PathVariable String userId) {
        return mediaService.getMediaByUserId(userId);
    }

    @GetMapping("/byProductId/{productId}")
    public List<MediaResponse> getByProductId(@PathVariable String productId) {
        return mediaService.getMediaByProductId(productId);
    }

    @PostMapping("/media")
    public ResponseEntity<?> createMedia(@RequestBody @Valid Media media, Principal principal) {
        try {
            Media createdMedia = mediaService.createMedia(media, principal.getName());
            return ResponseEntity.ok("Media created: " + createdMedia);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedia(@PathVariable String id, Principal principal) {
        try {
            mediaService.deleteMedia(id, principal.getName());
            return ResponseEntity.ok("Media deleted");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }
}
