package repository.impl;

import entity.User;
import entity.tiny.UserId;
import repository.InvalidIdException;
import repository.UniqueFieldException;
import repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * In memory representation of user repository.
 */
public class InMemoryUserRepository implements UserRepository {

    private Map<UserId, User> content = new HashMap<UserId, User>();

    private long idCounter = 0;

    public synchronized UserId add(User user) throws UniqueFieldException {

            user.setId(new UserId(idCounter++));

            content.put(user.getId(), user);

            return user.getId();
    }

    public User get(UserId userId) throws InvalidIdException {
        User user = content.get(userId);

        if (user != null) {
            return user;
        } else {
            throw new InvalidIdException("User with id = " + userId.getId() + " doesn't exist.");
        }
    }
}
