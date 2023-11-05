@Service
public class JwtService {
    public List<GrantedAuthority> extractAuthorities(String jwt) {
        Claims claims = extractAllClaims(jwt);
        Role role = claims.get("role", Role.class);
        // TEMPORARY
        if (role == null) {
            System.out.println("ROLE EXTRACTION NOT WORKING!");
            return Collections.emptyList();
        }
        return Collections.singletonList(new SimpleGrantedAuthority(role.toString()));
    }

    public String extractUserId(String jwt) {
        Claims claims = extractAllClaims(jwt);
        String userId = claims.get("userId", String.class);
        // TEMPORARY
        if (userId == null) {
            System.out.println("USER ID EXTRACTION NOT WORKING!");
            return "";
        }
        return userId;
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
