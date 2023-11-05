public class SecurityContextService {

    private final JwtService jwtService;


    public void setAuthentication(String jwt) {
        String username = jwtService.extractUsername(jwt);

        List<GrantedAuthority> authorities = jwtService.extractAuthorities(jwt);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                authorities
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
