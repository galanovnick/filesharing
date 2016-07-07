package repository;

import com.google.common.base.Optional;
import entity.User;

import java.util.Collection;

/**
 * Interface for user repository.
 */
public interface UserRepository {

    entity.tiny.UserId add(User user);

    User get(entity.tiny.UserId userId) throws InvalidIdException;

    Optional<User> getByUsername(String username);

    Optional<User> getByEmail(String email);

    Collection<User> getAll();
}
