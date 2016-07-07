package repository;

import com.google.common.base.Optional;
import entity.File;
import entity.tiny.FileId;

/**
 * Interface for files repository.
 */
public interface FileRepository {

    FileId add(File file);

    Optional<File> get(FileId fileId);
}
