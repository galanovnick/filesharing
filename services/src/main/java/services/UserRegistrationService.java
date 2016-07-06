package services;

import entity.User;


/**
 * Interface for user registration.
 */
public interface UserRegistrationService {

    /**
     * Registers user in system.
     * @param user - user to be registered.
     */
    void register(User user) throws DuplicateUserException;
}
