package buy01.msgateway.services;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import buy01.msgateway.models.media.MediaRequest;
import buy01.msgateway.models.media.MediaResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MediaServiceClient {

    private final RestTemplate restTemplate;

    public List<MediaResponse> getAllMedia() {
        ResponseEntity<List<MediaResponse>> response = restTemplate.getForEntity(
            "http://ms-media/api/media/all",
            null,
            new ParameterizedTypeReference<List<MediaResponse>>() {});

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to get all media");
        }

        return response.getBody();
    }

    public MediaResponse getMediaById(String id) {
        ResponseEntity<MediaResponse> response = restTemplate.getForEntity(
            "http://ms-media/api/media/" + id,
            null,
            MediaResponse.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to get media by id");
        }

        return response.getBody();
    }

    public List<MediaResponse> getMediaByProductId(String productId) {
        ResponseEntity<List<MediaResponse>> response = restTemplate.getForEntity(
            "http://ms-media/api/media/all/byProductId/" + productId,
            null,
            new ParameterizedTypeReference<List<MediaResponse>>() {});

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to get media by product id");
        }

        return response.getBody();
    }

    public MediaResponse createMedia(MediaRequest mediaRequest) {
        ResponseEntity<MediaResponse> response = restTemplate.postForEntity(
            "http://ms-media/api/media/createMedia",
            new HttpEntity<>(mediaRequest),
            MediaResponse.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to create media");
        }

        return response.getBody();
    }

    public MediaResponse updateMedia(String id, MediaRequest mediaRequest) {
        HttpEntity<MediaRequest> requestEntity = new HttpEntity<>(mediaRequest);

        ResponseEntity<MediaResponse> response = restTemplate.exchange(
            "http://ms-media/api/media/updateMedia/" + id,
            HttpMethod.PUT,
            requestEntity,
            MediaResponse.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to update media");
        }

        return response.getBody();
    }

    public void deleteMedia(String id) {
        ResponseEntity<Void> response = restTemplate.exchange(
            "http://ms-media/api/media/deleteMedia/" + id,
            HttpMethod.DELETE,
            null,
            Void.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to delete media");
        }
    }
}
