package services;

import com.google.common.base.Optional;
import entity.File;
import entity.User;
import entity.tiny.FileId;
import entity.tiny.LocationId;
import org.junit.After;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

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
    private final User user = new User("user", "user@mail.com", "pass");

    private AuthenticationToken token;

    @Before
    public void before() throws AuthenticationException, FileNotFoundException {

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

    @Test(expected = IllegalArgumentException.class)
    public void removeFiles() throws AuthenticationException {

        fileService.deleteFile(token, file.getId(), file.getOwnerId());

        fileService.getFileMeta(token, file.getId(), file.getOwnerId());
    }
    
    @Test
    public void beSafeInMultithreading() throws Exception {

        final ExecutorService executorService = Executors.newFixedThreadPool(50);

        final CountDownLatch countDownLatch = new CountDownLatch(50);

        List<Future<FileId>> futuresList = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            futuresList.add(executorService.submit(() -> {

                countDownLatch.countDown();
                countDownLatch.await();

                try {
                    File file1 = new File(new LocationId(0), fileContent.getName() , null, user.getId());

                    FileInputStream fileStream = new FileInputStream(fileContent);
                    fileService.deleteFile(token, fileService.add(token, file1, fileStream), user.getId());
                    fileStream.close();

                    File file2 = new File(new LocationId(0), fileContent.getName() , null, user.getId());
                    fileStream = new FileInputStream(fileContent);
                    fileService.add(token, file2, fileStream);
                    fileStream.close();

                    File file3 = new File(new LocationId(0), fileContent.getName() , null, user.getId());
                    fileStream = new FileInputStream(fileContent);
                    fileService.deleteFile(token, fileService.add(token, file3, fileStream), user.getId());
                    fileStream.close();

                    File file4 = new File(new LocationId(0), fileContent.getName() , null, user.getId());
                    fileStream = new FileInputStream(fileContent);
                    fileService.deleteFile(token, fileService.add(token, file4, fileStream), user.getId());
                    fileStream.close();
                } catch (AuthenticationException e) {
                    fail("Not safe in multithreading.");
                }

                return file.getId();
            }));
        }

        for (Future<FileId> elem : futuresList) {
            elem.get();
        }

        assertEquals(51, fileService.getAllMeta().size());
    }
    
    private void checkFileMeta(File expected, File actual) {
        assertEquals("Failed file meta return.", expected, actual);
    }

    private void checkFileContent(FileInputStream expected, ByteArrayInputStream actual) throws IOException {
        int expectedValue;
        int actualValue;
        try {
            while (((expectedValue = expected.read()) != -1)
                    && ((actualValue = actual.read()) != -1)) {
                assertEquals("Failed file content return.", expectedValue, actualValue);
            }
        } finally {
            expected.close();
        }
    }
}
