package repository.impl;

import com.google.common.base.Optional;
import entity.User;
import repository.InvalidIdException;
import repository.UserRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * "In memory" users repository.
 */
public class InMemoryUserRepository implements UserRepository {

    private Map<entity.tiny.UserId, User> content = new HashMap<>();

    private long idCounter = 0;

    public synchronized entity.tiny.UserId add(User user) {

        checkNotNull(user, "User cannot be null.");

        user.setId(new entity.tiny.UserId(idCounter++));

        content.put(user.getId(), user);

        return user.getId();
    }

    public User get(entity.tiny.UserId userId) throws InvalidIdException {
        checkNotNull(userId, "User id cannot be null.");

        User user = content.get(userId);

        if (user != null) {
            return user;
        } else {
            throw new InvalidIdException("User with id = " + userId.getId() + " doesn't exist.");
        }
    }

    public Collection<User> getAll() {
        return content.values();
    }

    @Override
    public Optional<User> getByUsername(String username) {
        for (User user : content.values()) {
            if (user.getUsername().equals(username)) {
                return Optional.of(user);
            }
        }
        return Optional.absent();
    }

    @Override
    public Optional<User> getByEmail(String email) {
        for (User user : content.values()) {
            if (user.getEmail().equals(email)) {
                return Optional.of(user);
            }
        }
        return Optional.absent();
    }
}
