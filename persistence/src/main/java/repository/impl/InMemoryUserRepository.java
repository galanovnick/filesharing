package repository.impl;

import com.google.common.base.Optional;
import entity.User;
import entity.tiny.UserId;
import repository.InvalidIdException;
import repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * "In memory" users repository.
 */
public class InMemoryUserRepository implements UserRepository {

    private final static Lock ID_LOCK = new ReentrantLock();

    private final ConcurrentMap<UserId, User> content = new ConcurrentHashMap<>();

    private long idCounter = 0;

    public UserId add(User user) {

        checkNotNull(user, "User cannot be null.");

        user.setId(new entity.tiny.UserId(nextId()));

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

    private long nextId() {
        ID_LOCK.lock();

        try{
            return idCounter++;
        } finally {
            ID_LOCK.unlock();
        }
    }
}
