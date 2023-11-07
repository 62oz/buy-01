package buy01.mediaservice.services;

import org.springframework.stereotype.Service;

import buy01.mediaservice.repositories.MediaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepository;

    public Boolean isOwner(String mediaId, String authenticatedId) {
        return mediaRepository.findById(mediaId)
                .map(media -> media.getUserId().equals(authenticatedId))
                .orElse(false);
    }
}
