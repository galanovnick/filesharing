package services.impl;

import entity.User;
import repository.UserRepository;
import services.DuplicateUserException;
import services.UserRegistrationService;

/**
 * Contains implementation of user registration in system.
 */
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRepository userRepository;

    public UserRegistrationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(User user) throws DuplicateUserException {

    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
