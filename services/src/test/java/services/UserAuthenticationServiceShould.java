package services;

import entity.File;
import entity.User;
import entity.tiny.FileId;
import entity.tiny.LocationId;
import entity.tiny.UserId;
import org.junit.Before;
import org.junit.Test;
import repository.impl.InMemoryUserAuthenticationRepository;
import repository.impl.InMemoryUserRepository;
import services.impl.AuthenticationToken;
import services.impl.UserAuthenticationServiceImpl;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class UserAuthenticationServiceShould {

    private final InMemoryUserRepository userRepository = new InMemoryUserRepository();

    private final InMemoryUserAuthenticationRepository inMemoryUserAuthenticationRepository
            = new InMemoryUserAuthenticationRepository();
    private final UserAuthenticationService userAuthenticationService
            = new UserAuthenticationServiceImpl(inMemoryUserAuthenticationRepository, userRepository);

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

        assertEquals("Failed authentication check.", true,
                userAuthenticationService.checkAuthentication(token, user.getId()));
    }

    @Test
    public void terminateAuthentication() throws AuthenticationException {
        AuthenticationToken token =
            userAuthenticationService.authenticateByEmail(user.getEmail(), user.getPassword());

        userAuthenticationService.terminateAuthentication(token);

        assertEquals("Failed authentication termination.", false,
                userAuthenticationService.checkAuthentication(token, user.getId()));
    }

    @Test
    public void beSafeInMultithreading() throws Exception {

        final ExecutorService executorService = Executors.newFixedThreadPool(50);

        final CountDownLatch countDownLatch = new CountDownLatch(50);

        List<Future<UserId>> futuresList = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            futuresList.add(executorService.submit(() -> {

                countDownLatch.countDown();
                countDownLatch.await();

                try {
                    userAuthenticationService.terminateAuthentication(
                            userAuthenticationService.authenticateByUsername(
                                    user.getUsername(), user.getPassword()));

                    userAuthenticationService.authenticateByEmail(user.getEmail(), user.getPassword());

                    userAuthenticationService.terminateAuthentication(
                            userAuthenticationService.authenticateByEmail(
                                    user.getEmail(), user.getPassword()));
                    userAuthenticationService.terminateAuthentication(
                            userAuthenticationService.authenticateByUsername(
                                    user.getUsername(), user.getPassword()));

                } catch (AuthenticationException e) {
                    fail("Not safe in multithreading.");
                }

                return user.getId();
            }));
        }

        for (Future<UserId> elem : futuresList) {
            elem.get();
        }
    }
}
