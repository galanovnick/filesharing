package repository;

/**
 * Throw to indicate attempt to insert duplicated unique field.
 */
public class UniqueFieldException extends Exception {

    public UniqueFieldException(String message) {
        super(message);
    }
}
