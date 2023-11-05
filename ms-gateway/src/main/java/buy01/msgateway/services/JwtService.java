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
