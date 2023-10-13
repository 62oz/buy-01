package buy01.authservice.models;

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
        private String username;
        private String password;
        private String salt;
        private Role role;
}
