package buy01.userservice.models.auth;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AuthRegistrationResponse {
    String requestId;
    Boolean isSuccessful;
}
