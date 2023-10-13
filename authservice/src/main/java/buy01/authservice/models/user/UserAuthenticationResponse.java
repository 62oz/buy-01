package buy01.authservice.models.user;

import org.springframework.security.core.userdetails.UserDetails;

import buy01.authservice.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthenticationResponse {
    private String requestId;
    private UserDetails userDetails;
    private Role role;
    private String avatar;
    private String salt;
}
