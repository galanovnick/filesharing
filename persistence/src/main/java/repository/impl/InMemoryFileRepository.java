package repository.impl;

import com.google.common.base.Optional;
import entity.File;
import entity.FileContent;
import entity.tiny.FileId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.FileRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * "In memory" files repository
 */
public class InMemoryFileRepository implements FileRepository {

    private final Logger log = LoggerFactory.getLogger(InMemoryFileRepository.class);

    private final Map<FileId, File> metaContent = new HashMap<>();

    private final Map<FileId, FileContent> filesContent = new HashMap<>();

    private long idCounter = 0;

    @Override
    public FileId add(File file, FileInputStream fileInputStream) {
        file.setId(new FileId(idCounter++));

        if (log.isDebugEnabled()) {
            log.debug("Adding file meta (file id = \"" + file.getId().get() + "\")");
        }

        metaContent.put(file.getId(), file);

        if (log.isDebugEnabled()) {
            log.debug("File meta added (file id = \"" + file.getId().get() + "\")");
        }

        try {
            acceptFileContent(file.getId(), fileInputStream);
        } catch (IOException e) {
            log.error("Specified file not found.", e);
            throw new IllegalStateException("File not found.");
        }

        return file.getId();
    }

    @Override
    public Optional<File> getFileMeta(FileId fileId) {

        File file = metaContent.get(fileId);

        return Optional.fromNullable(file);
    }

    @Override
    public Optional<ByteArrayInputStream> getFileContent(FileId fileId) {

        FileContent fileContent = filesContent.get(fileId);

        if (fileContent != null) {
            return Optional.of(new ByteArrayInputStream(fileContent.getContent()));
        }

        return Optional.absent();
    }


    @Override
    public Optional<File> removeFile(FileId fileId) {

        File removedFile = metaContent.remove(fileId);

        if (removedFile != null) {
            filesContent.remove(fileId);

            return Optional.of(removedFile);
        }

        return Optional.absent();
    }

    private void acceptFileContent(FileId fileId, FileInputStream fileInputStream) throws IOException {

        if (log.isDebugEnabled()) {
            log.debug("Adding file content (fileid = \"" + fileId.get() + "\")");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] contentPart = new byte[4096];
        int counter;

        while((counter = fileInputStream.read(contentPart)) != -1) {
            outputStream.write(contentPart, 0, counter);
        }

        filesContent.put(fileId, new FileContent(outputStream.toByteArray(), fileId));
    }
}
