package repository.impl;

import com.google.common.base.Optional;
import entity.File;
import entity.tiny.FileId;
import repository.FileRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * "In memory" files repository
 */
public class InMemoryFileRepository implements FileRepository {

    private final Map<FileId, File> content = new HashMap<>();

    private long idCounter = 0;

    @Override
    public FileId add(File file) {
        file.setId(new FileId(idCounter++));

        content.put(file.getId(), file);

        return file.getId();
    }

    @Override
    public Optional<File> get(FileId fileId) {

        File file = content.get(fileId);

        return Optional.fromNullable(file);
    }
}
