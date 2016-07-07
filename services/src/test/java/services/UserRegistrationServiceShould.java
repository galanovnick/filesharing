package services;

import entity.User;
import entity.tiny.UserId;
import org.junit.Test;
import repository.InvalidIdException;
import repository.impl.InMemoryUserRepository;
import services.impl.UserRegistrationServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UserRegistrationServiceShould {

    private UserRegistrationServiceImpl userRegistrationService
            = new UserRegistrationServiceImpl(new InMemoryUserRepository());

    @Test
    public void registerUsers() throws DuplicateUserException, InvalidIdException {

        User expected = new User("username", "user@mail.mail", "pass");

        userRegistrationService.register(expected);

        User actual = userRegistrationService.getUserRepository().get(expected.getId());

        assertEquals("Failed user registration.", expected, actual);
    }

    @Test(expected = DuplicateUserException.class)
    public void notRegisterDuplicatedUsers() throws DuplicateUserException {
        User someUser = new User("username", "user@mail.mail", "pass");

        userRegistrationService.register(someUser);
        userRegistrationService.register(someUser);
    }

    @Test
    public void beSafeInMultithreading() throws ExecutionException, InterruptedException {

        final ExecutorService executorService = Executors.newFixedThreadPool(50);

        final CountDownLatch countDownLatch = new CountDownLatch(50);

        List<Future<UserId>> futuresList = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            User user = new User("username", "user@mail.mail" + i, "pass");
            futuresList.add(executorService.submit(() -> {

                User user2 = new User("username", "user@mail.mail" + Thread.currentThread().getName(), "pass");

                countDownLatch.countDown();
                countDownLatch.await();

                userRegistrationService.register(user);
                userRegistrationService.register(user2);

                return user.getId();
            }));
        }

        for (Future<UserId> elem : futuresList) {
            elem.get();
        }
    }
}
