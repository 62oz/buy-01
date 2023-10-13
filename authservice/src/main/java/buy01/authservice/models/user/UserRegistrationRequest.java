package buy01.authservice.models.user;

import buy01.authservice.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {
    private String requestId;
    private String userId;
    private String username;
    private String email;
    private String password;
    private String salt;
    private Role role;
    private String avatar;
}