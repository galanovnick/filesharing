package services;

import entity.User;
import services.impl.AuthenticationToken;

/**
 * Interface for user authentication.
 */
public interface UserAuthenticationService {

    /**
     * Authenticates user by username and password.
     * @param username - username.
     * @param password - user password.
     * @return user authentication token.
     */
    AuthenticationToken authenticateByUsername(String username, String password)
            throws AuthenticationException;

    /**
     * Authenticates user by email and password.
     * @param email - user email.
     * @param password - user password.
     * @return user authentication token.
     */
    AuthenticationToken authenticateByEmail(String email, String password)
            throws AuthenticationException;

    /**
     * Makes authentication token invalid.
     * @param token - authentication token.
     */
    void terminateAuthentication(AuthenticationToken token);

    boolean checkAuthentication(AuthenticationToken token, User user);
}
