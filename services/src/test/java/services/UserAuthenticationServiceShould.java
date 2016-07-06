package services;

import entity.User;
import entity.UserAuthentication;
import entity.tiny.UserId;
import org.junit.Before;
import org.junit.Test;
import repository.impl.InMemoryUserAuthenticationRepository;
import repository.impl.InMemoryUserRepository;
import services.impl.AuthenticationToken;
import services.impl.UserAuthenticationServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserAuthenticationServiceShould {

    private final InMemoryUserRepository userRepository = new InMemoryUserRepository();

    private final UserAuthenticationService userAuthenticationService
            = new UserAuthenticationServiceImpl(new InMemoryUserAuthenticationRepository(), userRepository);

    private final User user = new User("user", "user@user.user", "pass");

    @Before
    public void before() {

        userRepository.add(user);
    }

    @Test
    public void authenticateUsersByEmail() throws AuthenticationException {

        AuthenticationToken token =
                userAuthenticationService.authenticateByEmail(user.getEmail(), user.getPassword());

        assertNotNull("Failed user authentication by email.", token);
    }

    @Test
    public void authenticateUsersByUsername() throws AuthenticationException {

        AuthenticationToken token =
                userAuthenticationService.authenticateByUsername(user.getUsername(), user.getPassword());

        assertNotNull("Failed user authentication by email.", token);
    }

    @Test
    public void checkAuthentication() throws AuthenticationException {
        AuthenticationToken token =
                userAuthenticationService.authenticateByEmail(user.getEmail(), user.getPassword());

        assertEquals("Failed authentication termination.", true,
                userAuthenticationService.checkAuthentication(token, user));
    }

    @Test
    public void terminateAuthentication() throws AuthenticationException {
        AuthenticationToken token =
            userAuthenticationService.authenticateByEmail(user.getEmail(), user.getPassword());

        userAuthenticationService.terminateAuthentication(token);

        assertEquals("Failed authentication termination.", false,
                userAuthenticationService.checkAuthentication(token, user));
    }

}
