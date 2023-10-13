package buy01.userservice.models.auth;

import org.springframework.security.core.userdetails.UserDetails;

import buy01.userservice.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthAuthenticationResponse {
    private String requestId;
    private UserDetails userDetails;
    private Role role;
    private String avatar;
}
