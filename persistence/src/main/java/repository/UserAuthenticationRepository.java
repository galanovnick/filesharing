package repository;

import entity.UserAuthentication;
import entity.tiny.UserAuthenticationId;

import java.util.Collection;

/**
 * Interface for user repository.
 */
public interface UserAuthenticationRepository {

    UserAuthenticationId add(UserAuthentication userAuthentication);

    UserAuthentication get(UserAuthenticationId id) throws InvalidIdException;

    void delete(UserAuthenticationId id) throws InvalidIdException;

    Collection<UserAuthentication> getAll();
}
