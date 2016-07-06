package services;

import entity.User;
import org.junit.Test;
import repository.InvalidIdException;
import repository.UserRepository;
import repository.impl.InMemoryUserRepository;
import services.impl.UserRegistrationServiceImpl;

import static org.junit.Assert.assertEquals;

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
    public void notRegisterDuplicatedUser() throws DuplicateUserException {
        User expected = new User("username", "user@mail.mail", "pass");

        userRegistrationService.register(expected);
    }
}
