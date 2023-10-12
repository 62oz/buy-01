package buy01.authservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthenticationResponse {
        String requestId;
        String username;
        String password;
        String salt;
        Role role;
}
