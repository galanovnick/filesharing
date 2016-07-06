package services;

/**
 * Throw to indicate attempt of unregistered user authentication.
 */
public class AuthenticationException extends Exception {

    public AuthenticationException(String message) {
        super(message);
    }
}
