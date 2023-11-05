package buy01.authservice.models;

import org.springframework.data.annotation.Id;

import buy01.authservice.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    private String id;
    @Id
    private String userId;
    private String username;
    private String email;
    private String password;
    private String salt;
    private Role role;
    private String token;
}
