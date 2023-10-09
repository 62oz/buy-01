package buy01.userservice.exception;

public class InvalidJwtTokenException extends RuntimeException {

    public InvalidJwtTokenException() {
        super("Invalid JWT token.");
    }

    public InvalidJwtTokenException(String message) {
        super(message);
    }
}
