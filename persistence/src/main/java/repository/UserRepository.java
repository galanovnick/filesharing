package repository;

import entity.User;
import entity.tiny.UserId;

/**
 * Interface for user repository.
 */
public interface UserRepository {

    UserId add(User user) throws UniqueFieldException;

    User get(UserId userId) throws InvalidIdException;
}
