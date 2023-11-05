package buy01.userservice.models;

import buy01.userservice.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthenticationResponse {
    private String userId;
    private String username;
    private String avatar;
    private Role role;
}
