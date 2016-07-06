package repository;

/**
 * Throw to indicate attempt to request data by negative or non-existent id.
 */
public class InvalidIdException extends Exception {

    public InvalidIdException(String message) {
        super(message);
    }
}
