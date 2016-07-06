package repository;

import entity.User;
import entity.tiny.UserId;

import java.util.Collection;
import java.util.Optional;

/**
 * Interface for user repository.
 */
public interface UserRepository {

    UserId add(User user);

    User get(UserId userId) throws InvalidIdException;

    Optional<User> getByUsername(String username);

    Optional<User> getByEmail(String email);

    Collection<User> getAll();
}
