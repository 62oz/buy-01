package buy01.authservice.services;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemReader;
import java.security.*;
import java.security.spec.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import buy01.authservice.enums.Role;
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
            Security.addProvider(new BouncyCastleProvider());

            byte[] privateKeyBytes = Files.readAllBytes(Paths.get(PRIVATE_KEY_PATH));
            PemReader pemReader = new PemReader(new StringReader(new String(privateKeyBytes)));
            byte[] pemContent = pemReader.readPemObject().getContent();
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(pemContent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

            byte[] publicKeyBytes = Files.readAllBytes(Paths.get(PUBLIC_KEY_PATH));
            pemReader = new PemReader(new StringReader(new String(publicKeyBytes)));
            pemContent = pemReader.readPemObject().getContent();
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(pemContent);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            keyPair = new KeyPair(publicKey, privateKey);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
            e.printStackTrace();
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

    public Role extractRole(String jwt) throws JwtException {
        try {
            return Role.valueOf((String) Jwts.parserBuilder().setSigningKey(keyPair.getPublic()).build().parseClaimsJws(jwt).getBody().get("role"));
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT. Please check your token.");
        }
    }

    public String extractUserId(String jwt) throws JwtException {
        try {
            return (String) Jwts.parserBuilder().setSigningKey(keyPair.getPublic()).build().parseClaimsJws(jwt).getBody().get("userId");
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
