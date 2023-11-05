package buy01.msgateway.services;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextService {

    private final JwtService jwtService;

    public SecurityContextService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

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
