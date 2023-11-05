package buy01.msgateway.services;

import java.security.KeyPair;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;

import java.util.Collections;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

import buy01.msgateway.enums.Role;

@Service
public class JwtService {
    private static final String PUBLIC_KEY_PATH = "./public_key.pem";
    private KeyPair keyPair;

    public List<GrantedAuthority> extractAuthorities(String jwt) {
        Claims claims = extractAllClaims(jwt);
        Role role = claims.get("role", Role.class);
        // TEMPORARY
        if (role == null) {
            System.out.println("ROLE EXTRACTION NOT WORKING!");
            throw new IllegalStateException("Role extraction not working!");
        }
        return Collections.singletonList(new SimpleGrantedAuthority(role.toString()));
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

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        String username = claims.getSubject();
        // TEMPORARY
        if (username == null) {
            System.out.println("USERNAME EXTRACTION NOT WORKING!");
            throw new IllegalStateException("Username extraction not working!");
        }
        return username;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(keyPair.getPublic())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

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
}
