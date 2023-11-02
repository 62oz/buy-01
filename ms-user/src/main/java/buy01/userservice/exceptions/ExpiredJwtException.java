package buy01.userservice.exceptions;

public class ExpiredJwtException extends RuntimeException {

    public ExpiredJwtException() {
        super("JWT token has expired.");
    }

    public ExpiredJwtException(String message) {
        super(message);
    }
}
