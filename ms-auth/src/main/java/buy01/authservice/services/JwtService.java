package buy01.authservice.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import buy01.authservice.models.Account;
import buy01.authservice.repositories.AccountRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Autowired
    private final AccountRepository accountRepository;

    private static final String PRIVATE_KEY_PATH = "./private_key.pem";
    private static final String PUBLIC_KEY_PATH = "./public_key.pem";
    private KeyPair keyPair;

    @PostConstruct
    public void init() {
        try {
            byte[] privateKeyBytes = Files.readAllBytes(Paths.get(PRIVATE_KEY_PATH));
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            byte[] publicKeyBytes = Files.readAllBytes(Paths.get(PUBLIC_KEY_PATH));
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);

            keyPair = new KeyPair(publicKey, privateKey);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Could not load keys", e);
        }
    }

    public String generateToken(Account account) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", account.getRole().toString());
        extraClaims.put("id", account.getUserId());
        extraClaims.put("userId", account.getUserId());

        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(account.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
            .compact();
    }

    public String extractUsername(String jwt) throws JwtException {
        try {
            return Jwts.parserBuilder().setSigningKey(keyPair.getPublic()).build().parseClaimsJws(jwt).getBody().getSubject();
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT. Please check your token.");
        }
    }

    public String validateToken(String jwt) {
        try {
            String username = extractUsername(jwt);
            Account account = accountRepository.findByUsername(username).get();
            if (!account.getToken().equals(jwt)) {
                throw new JwtException("Invalid JWT. Please check your token.");
            }
            // Update token
            String newToken = generateToken(account);
            account.setToken(newToken);
            accountRepository.save(account);
            return newToken;
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT. Please check your token.");
        }
    }
}
