package buy01.authservice.exception;

public class MissingJwtTokenException extends RuntimeException {

    public MissingJwtTokenException() {
        super("JWT token is missing.");
    }

    public MissingJwtTokenException(String message) {
        super(message);
    }
}
