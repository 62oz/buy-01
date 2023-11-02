package buy01.ms-auth.models.user;

import org.springframework.security.core.userdetails.UserDetails;

import buy01.ms-auth.enums.Role;
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
    private String userId;
    private UserDetails userDetails;
    private Role role;
    private String avatar;
    private String salt;
}
