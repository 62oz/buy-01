package buy01.productservice.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {
    private static final String PUBLIC_KEY_PATH = "./public_key.pem";
    private KeyPair keyPair;

        @PostConstruct
    public void init() {
        try {
            byte[] publicKeyBytes = Files.readAllBytes(Paths.get(PUBLIC_KEY_PATH));
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(x509KeySpec);

            keyPair = new KeyPair(publicKey, null);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Could not load keys", e);
        }
    }


    public String extractUserId(String jwt) {
        Claims claims = extractAllClaims(jwt);
        String userId = claims.get("userId", String.class);
        // TEMPORARY
        if (userId == null) {
            System.out.println("USER ID EXTRACTION NOT WORKING!");
            throw new IllegalStateException("User ID extraction not working!");
        }
        return userId;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(keyPair.getPublic())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
