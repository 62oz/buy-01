package buy01.ms-product.exceptions;

public class InvalidJwtTokenException extends RuntimeException {

    public InvalidJwtTokenException() {
        super("Invalid JWT token.");
    }

    public InvalidJwtTokenException(String message) {
        super(message);
    }
}
