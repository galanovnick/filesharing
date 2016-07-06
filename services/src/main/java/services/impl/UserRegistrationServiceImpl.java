package services.impl;

import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.UserRepository;
import services.DuplicateUserException;
import services.UserRegistrationService;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Contains implementation of user registration in system.
 */
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final Logger log = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);

    private final UserRepository userRepository;

    public UserRegistrationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(User user) throws DuplicateUserException {
        checkNotNull(user, "User cannot be null.");

        Collection<User> users = userRepository.getAll();

        for (User u : users) {
            if (u.getEmail().equals(user.getEmail())) {
                log.error("User with email = \"" + u.getEmail() + "\" already exist.");
                throw new DuplicateUserException("User with such email already exist.");
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("User(email=\"" + user.getEmail() + "\") added.");
        }
        userRepository.add(user);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
