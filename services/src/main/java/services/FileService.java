package services;

import entity.File;
import entity.tiny.FileId;
import entity.tiny.UserId;
import services.impl.AuthenticationToken;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.util.Collection;

/**
 * Interface for file management.
 */
public interface FileService {

    /**
     * Add file with specified user as owner.
     * @param file - file to be added.
     */
    FileId add(AuthenticationToken token, File file, FileInputStream inputStream)
            throws AuthenticationException;

    /**
     * Returns file content as stream.
     * @param token - authentication token
     * @param fileId - specified file meta
     * @return file content
     * @throws AuthenticationException
     */
    ByteArrayInputStream getFileContent(AuthenticationToken token, FileId fileId, UserId userId)
            throws AuthenticationException;

    /**
     * Returns file meta.
     * @param token - authentication token
     * @param fileId - file id
     * @return file meta.
     * @throws AuthenticationException
     */
    File getFileMeta(AuthenticationToken token, FileId fileId, UserId userId)
            throws AuthenticationException;

    /**
     * Delete specified file by id.
     * @param token - authentication token
     * @param fileId - specified file id
     * @param userId - user id
     */
    void deleteFile(AuthenticationToken token, FileId fileId, UserId userId);

    Collection<File> getAllMeta();
}
