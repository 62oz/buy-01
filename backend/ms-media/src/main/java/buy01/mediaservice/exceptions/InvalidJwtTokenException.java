package buy01.mediaservice.exceptions;

public class InvalidJwtTokenException extends RuntimeException {

    public InvalidJwtTokenException() {
        super("Invalid JWT token.");
    }

    public InvalidJwtTokenException(String message) {
        super(message);
    }
}
