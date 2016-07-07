package services;

import com.google.common.base.Optional;
import entity.File;
import entity.tiny.LocationId;
import entity.tiny.UserId;
import org.junit.Test;
import repository.FileRepository;
import repository.impl.InMemoryFileRepository;
import services.impl.FileServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FileServiceShould {

    private final FileRepository fileRepository = new InMemoryFileRepository();

    private final FileService fileService = new FileServiceImpl(fileRepository);

    private final File file = new File(new LocationId(0), "file", null, new UserId(0));


    @Test
    public void addFiles() {

        fileService.add(file);

        Optional<File> actual = fileRepository.get(file.getId());

        if (actual.isPresent()) {
            assertEquals("Failed file addition.", file, actual.get());
        } else {
            fail();
        }
    }
}
