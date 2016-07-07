package services;

import entity.File;
import entity.tiny.LocationId;
import entity.tiny.UserId;
import org.junit.Test;
import services.impl.FileServiceImpl;

import static org.junit.Assert.assertEquals;

public class FileServiceShould {

    private final FileService fileService = new FileServiceImpl();

    private final File file = new File(new LocationId(0), "file", null, new UserId(0));


    @Test
    public void addFiles() {

        fileService.add(file);

        assertEquals("Failed file addition.", file);

    }
}
