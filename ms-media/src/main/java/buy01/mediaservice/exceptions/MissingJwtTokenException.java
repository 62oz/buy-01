package buy01.ms-media.exceptions;

public class MissingJwtTokenException extends RuntimeException {

    public MissingJwtTokenException() {
        super("JWT token is missing.");
    }

    public MissingJwtTokenException(String message) {
        super(message);
    }
}
