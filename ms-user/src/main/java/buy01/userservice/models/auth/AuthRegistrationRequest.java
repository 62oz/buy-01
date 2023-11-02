package buy01.ms-user.models.auth;

import buy01.ms-user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRegistrationRequest {
    private String requestId;
    private String username;
    private String email;
    private String password;
    private String salt;
    private Role role;
    private String avatar;
}