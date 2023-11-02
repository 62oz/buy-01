package buy01.ms-user.exceptions;

public class ExpiredJwtException extends RuntimeException {

    public ExpiredJwtException() {
        super("JWT token has expired.");
    }

    public ExpiredJwtException(String message) {
        super(message);
    }
}
