package services;

/**
 * Throw to indicate that such user is already registered.
 */
public class DuplicateUserException extends Exception {

    public DuplicateUserException(String message) {
        super(message);
    }
}
