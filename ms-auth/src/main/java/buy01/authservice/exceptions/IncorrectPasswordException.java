package buy01.ms-auth.exceptions;

import org.springframework.security.core.AuthenticationException;

public class IncorrectPasswordException extends AuthenticationException {
    public IncorrectPasswordException(String msg) {
        super(msg);
    }
}
