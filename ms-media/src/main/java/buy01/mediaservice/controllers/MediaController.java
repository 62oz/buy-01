package buy01.ms-media.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
