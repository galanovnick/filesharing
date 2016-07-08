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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * "In memory" files repository
 */
public class InMemoryFileRepository implements FileRepository {

    private final static Lock ID_LOCK = new ReentrantLock();

    private final Logger log = LoggerFactory.getLogger(InMemoryFileRepository.class);

    private final ConcurrentMap<FileId, File> metaContent = new ConcurrentHashMap<>();

    private final Map<FileId, FileContent> filesContent = new HashMap<>();

    private long idCounter = 0;

    @Override
    public FileId add(File file, FileInputStream fileInputStream) {

        checkNotNull(file, "File cannot be null.");
        checkNotNull(fileInputStream, "File input stream cannot be null.");

        file.setId(new FileId(nextId()));

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

        checkNotNull(fileId, "File id cannot be null.");

        File file = metaContent.get(fileId);

        return Optional.fromNullable(file);
    }

    @Override
    public Optional<ByteArrayInputStream> getFileContent(FileId fileId) {

        checkNotNull(fileId, "File id cannot be null.");

        FileContent fileContent = filesContent.get(fileId);

        if (fileContent != null) {
            return Optional.of(new ByteArrayInputStream(fileContent.getContent()));
        }

        return Optional.absent();
    }


    @Override
    public Optional<File> removeFile(FileId fileId) {

        checkNotNull(fileId, "File id cannot be null.");

        File removedFile = metaContent.remove(fileId);

        if (removedFile != null) {
            filesContent.remove(fileId);

            return Optional.of(removedFile);
        }
        return Optional.absent();
    }

    @Override
    public Collection<File> getAllMeta() {
        return metaContent.values();
    }

    private void acceptFileContent(FileId fileId, FileInputStream fileInputStream) throws IOException {

        if (log.isDebugEnabled()) {
            log.debug("Adding file content (file id = \"" + fileId.get() + "\")");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] contentPart = new byte[4096];
        int counter;

        try {
            while ((counter = fileInputStream.read(contentPart)) != -1) {
                outputStream.write(contentPart, 0, counter);
            }
        } finally {
            fileInputStream.close();
        }

        filesContent.put(fileId, new FileContent(outputStream.toByteArray(), fileId));
    }

    private long nextId() {
        ID_LOCK.lock();

        try {
            return idCounter++;
        } finally {
            ID_LOCK.unlock();
        }
    }
}
