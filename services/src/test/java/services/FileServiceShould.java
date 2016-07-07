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
import java.io.FileNotFoundException;
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
    public void before() throws AuthenticationException, FileNotFoundException {

        User user = new User("user", "user@mail.com", "pass");

        userRepository.add(new User("user", "user@mail.com", "pass"));

        file = new File(new LocationId(0), fileContent.getName() , null, user.getId());

        token = authenticationService.authenticateByUsername("user", "pass");

        FileInputStream fileStream = new FileInputStream(fileContent);

        fileService.add(token, file, fileStream);
    }

    @Test
    public void addFiles() throws IOException, AuthenticationException {

        Optional<File> actual = fileRepository.getFileMeta(file.getId());

        Optional<ByteArrayInputStream> actualContent = fileRepository.getFileContent(file.getId());

        if (actual.isPresent() && actualContent.isPresent()) {
            checkFileMeta(file, actual.get());

            FileInputStream fileStream = new FileInputStream(fileContent);

            checkFileContent(fileStream, actualContent.get());
        } else {
            if (!actual.isPresent()) {
                fail("Failed file meta addition. No meta added.");
            } else {
                fail("Failed file content addition. No content added.");
            }
        }
    }

    @Test
    public void provideFilesMeta() throws AuthenticationException {
        File fileMeta = fileService.getFileMeta(token, file.getId(), file.getOwnerId());

        checkFileMeta(file, fileMeta);
    }

    @Test
    public void provideFilesContent() throws IOException, AuthenticationException {
        ByteArrayInputStream content = fileService.getFileContent(token, file.getId(), file.getOwnerId());


        FileInputStream fileInputStream = new FileInputStream(fileContent);
        checkFileContent(fileInputStream, content);

    }

    private void checkFileMeta(File expected, File actual) {
        assertEquals("Failed file meta return.", expected, actual);
    }

    private void checkFileContent(FileInputStream expected, ByteArrayInputStream actual) throws IOException {
        int expectedValue;
        int actualValue;
        while (((expectedValue = expected.read()) != -1)
                && ((actualValue = actual.read()) != -1)) {
            assertEquals("Failed file content return.", expectedValue, actualValue);
        }
    }
}
