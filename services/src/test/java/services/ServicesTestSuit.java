package services;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        FileServiceShould.class,
        UserAuthenticationServiceShould.class,
        UserRegistrationServiceShould.class
})
public class ServicesTestSuit {
}
