package repository;

import entity.User;
import entity.tiny.UserId;

import java.util.Collection;

/**
 * Interface for user repository.
 */
public interface UserRepository {

    UserId add(User user);

    User get(UserId userId) throws InvalidIdException;

    Collection<User> getAll();
}
