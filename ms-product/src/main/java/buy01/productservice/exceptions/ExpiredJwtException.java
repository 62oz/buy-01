package buy01.ms-product.exceptions;

public class ExpiredJwtException extends RuntimeException {

    public ExpiredJwtException() {
        super("JWT token has expired.");
    }

    public ExpiredJwtException(String message) {
        super(message);
    }
}
