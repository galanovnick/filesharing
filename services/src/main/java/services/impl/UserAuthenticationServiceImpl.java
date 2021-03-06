package services.impl;

import com.google.common.base.Optional;
import entity.User;
import entity.UserAuthentication;
import entity.tiny.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.InvalidIdException;
import repository.UserAuthenticationRepository;
import repository.UserRepository;
import services.AuthenticationException;
import services.UserAuthenticationService;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Contains implementation of user authentication in system.
 */
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private final Logger log = LoggerFactory.getLogger(UserAuthenticationServiceImpl.class);

    private final UserAuthenticationRepository userAuthenticationRepository;
    private final UserRepository userRepository;

    public UserAuthenticationServiceImpl(UserAuthenticationRepository userAuthenticationRepository,
                                         UserRepository userRepository) {

        this.userAuthenticationRepository = userAuthenticationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AuthenticationToken authenticateByUsername(String username, String password)
            throws AuthenticationException {
        if (log.isDebugEnabled()) {
            log.debug("Trying to authenticate by username(username=\"" + username + "\").");
        }

        checkNotNull(username, "Username cannot be null.");
        checkNotNull(password, "Password cannot be null.");
        checkArgument(username.length() != 0, "Username cannot has 0 length");
        checkArgument(password.length() != 0, "Password cannot has 0 length");

        Optional<User> user = userRepository.getByUsername(username);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            AuthenticationToken token = new AuthenticationToken("token " + user.get().getId().getId());

            UserAuthentication userAuthentication = new UserAuthentication(
                    user.get().getId(), token.getToken());

            userAuthenticationRepository.add(userAuthentication);

            if (log.isDebugEnabled()) {
                log.debug("User \"" + username + "\" authenticated. Token=\"" + token.getToken() + "\"");
            }

            return token;
        } else {
            if (log.isDebugEnabled()) {
                log.debug("User with such password or username doesn't exist.");
            }
            throw new AuthenticationException("User with such parameters doesn't exist.");
        }
    }

    @Override
    public AuthenticationToken authenticateByEmail(String email, String password)
            throws AuthenticationException {

        if (log.isDebugEnabled()) {
            log.debug("Trying to authenticate by email(email=\"" + email + "\").");
        }
        checkNotNull(email, "Email cannot be null.");
        checkNotNull(password, "Password cannot be null.");
        checkArgument(email.length() != 0, "Email cannot has 0 length");
        checkArgument(password.length() != 0, "Password cannot has 0 length");


        Optional<User> user = userRepository.getByEmail(email);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            AuthenticationToken token = new AuthenticationToken("token " + user.get().getId().getId());

            UserAuthentication userAuthentication = new UserAuthentication(
                    user.get().getId(), token.getToken());

            userAuthenticationRepository.add(userAuthentication);

            if (log.isDebugEnabled()) {
                log.debug("User \"" + email + "\" authenticated. Token=\"" + token.getToken() + "\"");
            }

            return token;
        } else {
            if (log.isDebugEnabled()) {
                log.debug("User with such password or email doesn't exist.");
            }
            throw new AuthenticationException("User with such parameters doesn't exist.");
        }
    }

    @Override
    public void terminateAuthentication(AuthenticationToken token) {
        if (log.isDebugEnabled()) {
            log.debug("Trying to terminate authentication with token = \"" + token.getToken() + "\"");
        }

        checkNotNull(token, "Token cannot be null.");

        try {
            if (log.isDebugEnabled()) {
                log.debug("Authentication terminated.");
            }

            userAuthenticationRepository.deleteByToken(token.getToken());
        } catch (InvalidIdException e) {
            log.error("Attempt to terminate non-existent authentication.");

            throw new IllegalArgumentException("Invalid authentication token.");
        }
    }

    @Override
    public boolean checkAuthentication(AuthenticationToken token, UserId userId) {

        checkNotNull(token, "Token cannot be null.");
        checkNotNull(userId, "User id cannot be null");

        final User user;
        try {
            user = userRepository.get(userId);
        } catch (InvalidIdException e) {
            throw new IllegalArgumentException("Invalid user id.");
        }

        if (log.isDebugEnabled()) {
            log.debug("Trying to check authentication for user=\"" + user.getUsername()
                    + "\" with token = \"" + token.getToken() + "\".");
        }

        Optional<UserAuthentication> authByToken = userAuthenticationRepository.getByToken(token.getToken());

        if (authByToken.isPresent() && authByToken.get().getUserId().equals(userId)) {
            if (log.isDebugEnabled()) {
                log.debug("Authentication successfully checked.");
            }
            return true;
        }
        if (!authByToken.isPresent()) {
            if (log.isDebugEnabled()) {
                log.debug("Failed authentication. No authenticated tokens found (token = \"" + token.getToken() + "\")");
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Failed authentication. No authenticated tokens for " +
                        "specified user found (user id = \"" + userId.getId() + "\")");
            }
        }
        return false;
    }
}
