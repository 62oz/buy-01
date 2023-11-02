package buy01.mediaservice.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import buy01.mediaservice.config.JwtService;
import buy01.mediaservice.enums.Role;
import buy01.mediaservice.exceptions.ResourceNotFoundException;
import buy01.mediaservice.repositories.MediaRepository;
import buy01.mediaservice.models.Media;
import buy01.mediaservice.models.MediaResponse;

public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;
    private JwtService jwtService;

    public List<MediaResponse> getAllMedia() {
        List<Media> media = mediaRepository.findAll();
        List<MediaResponse> mediaResponses = media.stream()
                                                        .map(this::mapToMediaResponse)
                                                        .collect(Collectors.toList());
        return mediaResponses;
    }

    public MediaResponse getMediaById(String id) {
        Media media = mediaRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Media not found for this id :: " + id));
        return mapToMediaResponse(media);
    }

    private MediaResponse mapToMediaResponse(Media media) {
        String token = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String userId = jwtService.extractUserId(token);
        Role role = Role.valueOf(jwtService.extractRole(token));

        MediaResponse.MediaResponseBuilder responseBuilder = MediaResponse.builder()
                                                                                .id("hidden")
                                                                                .imagePath(media.getImagePath())
                                                                                .productId("hidden")
                                                                                .userId("hidden");

        if (token != null) {
            boolean isAdmin = role.equals(Role.ROLE_ADMIN);
            boolean isOwner = userId.equals(media.getUserId());

            if (isAdmin || isOwner) {
                responseBuilder.id(media.getId())
                                .productId(media.getProductId())
                                .userId(media.getUserId());
            }
        }

        return responseBuilder.build();
    }
}