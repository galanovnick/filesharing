package repository.impl;

import entity.User;
import entity.tiny.UserId;
import repository.InvalidIdException;
import repository.UserRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * In memory representation of user repository.
 */
public class InMemoryUserRepository implements UserRepository {

    private Map<UserId, User> content = new HashMap<>();

    private long idCounter = 0;

    public synchronized UserId add(User user) {

        checkNotNull(user, "User cannot be null.");

        user.setId(new UserId(idCounter++));

        content.put(user.getId(), user);

        return user.getId();
    }

    public User get(UserId userId) throws InvalidIdException {
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
}
