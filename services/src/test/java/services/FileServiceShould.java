package services;

import com.google.common.base.Optional;
import entity.File;
import entity.User;
import entity.tiny.LocationId;
import org.junit.Before;
import org.junit.Test;
import repository.FileRepository;
import repository.UserRepository;
import repository.impl.InMemoryFileRepository;
import repository.impl.InMemoryUserAuthenticationRepository;
import repository.impl.InMemoryUserRepository;
import services.impl.AuthenticationToken;
import services.impl.FileServiceImpl;
import services.impl.UserAuthenticationServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FileServiceShould {

    private final FileRepository fileRepository = new InMemoryFileRepository();

    private final UserRepository userRepository = new InMemoryUserRepository();
    private final UserAuthenticationService authenticationService
            = new UserAuthenticationServiceImpl(new InMemoryUserAuthenticationRepository(),
            userRepository);

    private final FileService fileService = new FileServiceImpl(fileRepository, authenticationService);

    private final java.io.File fileContent = new java.io.File("build.gradle");

    private File file;

    private AuthenticationToken token;

    @Before
    public void before() throws AuthenticationException {

        User user = new User("user", "user@mail.com", "pass");

        userRepository.add(new User("user", "user@mail.com", "pass"));

        file = new File(new LocationId(0), fileContent.getName() , null, user.getId());

        token = authenticationService.authenticateByUsername("user", "pass");
    }

    @Test
    public void addFiles() throws IOException, AuthenticationException {


        FileInputStream fileStream = new FileInputStream(fileContent);

        fileService.add(token, file, fileStream);

        Optional<File> actual = fileRepository.getFileMeta(file.getId());

        Optional<ByteArrayInputStream> actualContent = fileRepository.getFileContent(file.getId());

        if (actual.isPresent() && actualContent.isPresent()) {
            checkFileMeta(file, actual.get());

            fileStream = new FileInputStream(fileContent);

            checkFileContent(fileStream, actualContent.get());
        } else {
            if (!actual.isPresent()) {
                fail("Failed file meta addition. No meta added.");
            } else {
                fail("Failed file content addition. No content added.");
            }
        }
    }

    private void checkFileMeta(File expected, File actual) {
        assertEquals("Failed file meta addition.", expected, actual);
    }

    private void checkFileContent(FileInputStream expected, ByteArrayInputStream actual) throws IOException {
        int expectedValue;
        int actualValue;
        while (((expectedValue = expected.read()) != -1)
                && ((actualValue = actual.read()) != -1)) {
            assertEquals("Failed file content addition.", expectedValue, actualValue);
        }
    }
}
