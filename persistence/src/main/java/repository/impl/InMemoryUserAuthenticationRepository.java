package repository.impl;

import com.google.common.base.Optional;
import entity.UserAuthentication;
import entity.tiny.UserAuthenticationId;
import repository.InvalidIdException;
import repository.UserAuthenticationRepository;
import services.impl.AuthenticationToken;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * "In memory" user authentication repository.
 */
public class InMemoryUserAuthenticationRepository implements UserAuthenticationRepository {

    private Map<UserAuthenticationId, UserAuthentication> content = new HashMap<>();

    private long idCounter = 0;

    @Override
    public UserAuthenticationId add(UserAuthentication userAuthentication) {

        checkNotNull(userAuthentication, "UserAuthentication object cannot be null.");

        userAuthentication.setId(new UserAuthenticationId(idCounter++));

        content.put(userAuthentication.getId(), userAuthentication);

        return userAuthentication.getId();
    }

    @Override
    public Optional<UserAuthentication> getByToken(AuthenticationToken token) {

        checkNotNull(token, "Authentication token cannot be null.");

        for (UserAuthentication userAuth : content.values()) {
            if (userAuth.getToken().equals(token)) {
                return Optional.of(userAuth);
            }
        }

        return Optional.absent();
    }

    @Override
    public synchronized void deleteByToken(AuthenticationToken token) throws InvalidIdException {

        checkNotNull(token, "Authentication token cannot be null.");

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
