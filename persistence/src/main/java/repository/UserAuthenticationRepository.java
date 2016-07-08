package repository;

import com.google.common.base.Optional;
import entity.UserAuthentication;
import entity.tiny.UserAuthenticationId;

import java.util.Collection;

/**
 * Interface for user repository.
 */
public interface UserAuthenticationRepository {

    UserAuthenticationId add(UserAuthentication userAuthentication);

    Optional<UserAuthentication> getByToken(String token);

    void deleteByToken(String token) throws InvalidIdException;

    Collection<UserAuthentication> getAll();
}
