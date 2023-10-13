package buy01.userservice.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AuthRegistrationResponse {
    String requestId;
    Boolean isSuccessful;
}
