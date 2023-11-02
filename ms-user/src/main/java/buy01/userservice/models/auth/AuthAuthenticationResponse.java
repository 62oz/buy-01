package buy01.ms-user.models.auth;

import org.springframework.security.core.userdetails.UserDetails;

import buy01.ms-user.enums.Role;
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
    private String userId;
    private UserDetails userDetails;
    private Role role;
    private String avatar;
}
