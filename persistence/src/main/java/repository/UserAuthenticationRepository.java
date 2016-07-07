package repository;

import com.google.common.base.Optional;
import entity.UserAuthentication;
import entity.tiny.UserAuthenticationId;
import services.impl.AuthenticationToken;

import java.util.Collection;

/**
 * Interface for user repository.
 */
public interface UserAuthenticationRepository {

    UserAuthenticationId add(UserAuthentication userAuthentication);

    Optional<UserAuthentication> getByToken(AuthenticationToken token);

    void deleteByToken(AuthenticationToken token) throws InvalidIdException;

    Collection<UserAuthentication> getAll();
}
