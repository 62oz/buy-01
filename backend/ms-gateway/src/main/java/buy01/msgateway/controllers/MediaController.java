package buy01.msgateway.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import buy01.msgateway.models.media.MediaRequest;
import buy01.msgateway.models.media.MediaResponse;
import buy01.msgateway.services.MediaServiceClient;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/media")
public class MediaController {

    private final MediaServiceClient mediaServiceClient;

    @GetMapping("/all")
    public ResponseEntity<List<MediaResponse>> getAllMedia() {
        return ResponseEntity.ok(mediaServiceClient.getAllMedia());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaResponse> getMediaById(String id) {
        return ResponseEntity.ok(mediaServiceClient.getMediaById(id));
    }

    @GetMapping("/all/byProductId/{productId}")
    public ResponseEntity<List<MediaResponse>> getMediaByProductId(String productId) {
        return ResponseEntity.ok(mediaServiceClient.getMediaByProductId(productId));
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\") or hasAuthority(\"ROLE_SELLER\")")
    @PostMapping("/createMedia")
    public ResponseEntity<MediaResponse> createMedia(MediaRequest mediaRequest) {
        return ResponseEntity.ok(mediaServiceClient.createMedia(mediaRequest));
    }

    @PreAuthorize("hasAuthority(\"ROLE_ADMIN\") or hasAuthority(\"ROLE_SELLER\") or  or #id == principal.id")
    @PostMapping("/updateMedia/{id}")
    public ResponseEntity<MediaResponse> updateMedia(String id, MediaRequest mediaRequest) {
        return ResponseEntity.ok(mediaServiceClient.updateMedia(id, mediaRequest));
    }
}
