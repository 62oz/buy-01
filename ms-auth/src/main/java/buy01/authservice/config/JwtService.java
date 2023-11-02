package buy01.ms-auth.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import buy01.ms-auth.exceptions.InvalidJwtTokenException;
import buy01.ms-auth.models.user.UserAuthenticationResponse;
import buy01.ms-auth.models.user.UserRegistrationRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY = "9e4ac61feadc39c7494c7db0aa739c8e0935b171e48ab4a9b443fc76c2df123d";

    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            throw new InvalidJwtTokenException("Invalid JWT token.");
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserAuthenticationResponse userDto) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", userDto.getRole().toString());
        extraClaims.put("avatar", userDto.getAvatar());
        extraClaims.put("salt", userDto.getSalt());
        extraClaims.put("id", userDto.getUserId());

        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDto.getUserDetails().getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }


    public String generateToken(UserRegistrationRequest userDto) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", userDto.getRole().toString());
        extraClaims.put("avatar", userDto.getAvatar());
        extraClaims.put("salt", userDto.getSalt());
        extraClaims.put("id", userDto.getUserId());

        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDto.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
        .parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
