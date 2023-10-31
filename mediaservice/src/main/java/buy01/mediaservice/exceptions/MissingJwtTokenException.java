package buy01.mediaservice.exceptions;

public class MissingJwtTokenException extends RuntimeException {

    public MissingJwtTokenException() {
        super("JWT token is missing.");
    }

    public MissingJwtTokenException(String message) {
        super(message);
    }
}
