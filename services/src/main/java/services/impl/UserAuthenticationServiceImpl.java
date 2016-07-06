package services.impl;

import entity.User;
import services.UserAuthenticationService;

/**
 * Contains implementation of user authentication in system.
 */
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    @Override
    public AuthenticationToken authenticateByUsername(String username, String password) {
        return null;
    }

    @Override
    public AuthenticationToken authenticateByEmail(String email, String password) {
        return null;
    }

    @Override
    public void terminateAuthentication(AuthenticationToken token) {

    }

    @Override
    public boolean checkAuthentication(AuthenticationToken token, User user) {
        return false;
    }
}
