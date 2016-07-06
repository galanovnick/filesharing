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
import java.util.Optional;

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
    public Optional<UserAuthentication> getByToken(AuthenticationToken token) {
        for (UserAuthentication userAuth : content.values()) {
            if (userAuth.getToken().equals(token)) {
                return Optional.of(userAuth);
            }
        }

        return Optional.empty();
    }

    @Override
    public synchronized void deleteByToken(AuthenticationToken token) throws InvalidIdException {
        for (UserAuthentication userAuth : content.values()) {
            if (userAuth.getToken().equals(token)) {
                content.remove(userAuth.getId());
                return;
            }
        }

        throw new InvalidIdException("User authentication with such token doesn't exist.");
    }

    @Override
    public Collection<UserAuthentication> getAll() {
        return content.values();
    }
}
