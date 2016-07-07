package repository;

import com.google.common.base.Optional;
import entity.File;
import entity.tiny.FileId;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;

/**
 * Interface for files repository.
 */
public interface FileRepository {

    FileId add(File file, FileInputStream fileInputStream);

    Optional<File> getFileMeta(FileId fileId);

    Optional<ByteArrayInputStream> getFileContent(FileId fileId);
}
