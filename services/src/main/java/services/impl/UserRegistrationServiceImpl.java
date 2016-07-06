package services.impl;

import entity.User;
import repository.UniqueFieldException;
import repository.UserRepository;
import services.DuplicateUserException;
import services.UserRegistrationService;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Contains implementation of user registration in system.
 */
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRepository userRepository;

    public UserRegistrationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(User user) throws DuplicateUserException {
        checkNotNull(user, "User cannot be null.");

        Collection<User> users = userRepository.getAll();

        for (User u : users) {
            if (u.getEmail().equals(user.getEmail())) {
                throw new DuplicateUserException("User with such email already exist.");
            }
        }

        userRepository.add(user);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
