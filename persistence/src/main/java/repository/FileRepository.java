package repository;

import com.google.common.base.Optional;
import entity.File;
import entity.tiny.FileId;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.util.Collection;

/**
 * Interface for files repository.
 */
public interface FileRepository {

    /**
     * Insert new file with "file" meta and content from "fileInputStream".
     * @param file - new file meta
     * @param fileInputStream - provides content
     * @return inserted file id
     */
    FileId add(File file, FileInputStream fileInputStream);

    /**
     * Returns file meta.
     * @param fileId - specified file id.
     * @return file meta
     */
    Optional<File> getFileMeta(FileId fileId);

    /**
     * Removes file.
     * @param fileId - file id
     * @return removed file meta
     */
    Optional<File> removeFile(FileId fileId);

    /**
     * Returns stream with file content.
     * @param fileId - file id
     * @return stream
     */
    Optional<ByteArrayInputStream> getFileContent(FileId fileId);

    Collection<File> getAllMeta();
}
