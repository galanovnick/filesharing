package repository.impl;

import entity.User;
import entity.UserAuthentication;
import entity.tiny.UserAuthenticationId;
import entity.tiny.UserId;
import repository.InvalidIdException;
import repository.UserAuthenticationRepository;
import services.impl.AuthenticationToken;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * In memory representation of user authentication repository.
 */
public class InMemoryUserAuthenticationRepository implements UserAuthenticationRepository {

    private Map<UserAuthenticationId, UserAuthentication> content = new HashMap<>();

    private long idCounter = 0;

    @Override
    public synchronized UserAuthenticationId add(UserAuthentication userAuthentication) {
        checkNotNull(userAuthentication);

        userAuthentication.setId(new UserAuthenticationId(idCounter++));

        content.put(userAuthentication.getId(), userAuthentication);

        return userAuthentication.getId();
    }

    @Override
    public UserAuthentication get(UserAuthenticationId id) throws InvalidIdException {
        checkNotNull(id);

        UserAuthentication userAuthentication = content.get(id);

        if (userAuthentication != null) {
            return userAuthentication;
        } else {
            throw new InvalidIdException("UserAuthentication with id = " + id.getId() + " doesn't exist.");
        }
    }

    @Override
    public void delete(UserAuthenticationId id) throws InvalidIdException {
        checkNotNull(id);

        if (content.remove(id) == null) {
            throw new InvalidIdException("UserAuthentication with id = " + id.getId() + " doesn't exist.");
        }
    }

    @Override
    public Collection<UserAuthentication> getAll() {
        return content.values();
    }
}
