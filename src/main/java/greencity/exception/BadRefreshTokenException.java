package greencity.exception;

/**
 * Exception that we get when user trying to refresh access token with bad refresh token.
 *
 * @author Nazar Stasyuk
 * @version 1.0
 */
public class BadRefreshTokenException extends RuntimeException {
    /**
     * Generated javadoc, must be replaced with real one.
     */
    public BadRefreshTokenException(String message) {
        super(message);
    }
}
