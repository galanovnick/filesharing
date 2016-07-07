package services;

import entity.File;
import entity.tiny.FileId;
import services.impl.AuthenticationToken;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;

/**
 * Interface for file management.
 */
public interface FileService {

    /**
     * Add file with specified user as owner.
     * @param file - file to be added.
     */
    void add(AuthenticationToken token, File file, FileInputStream inputStream)
            throws AuthenticationException;

    ByteArrayInputStream getFileContent(AuthenticationToken token, FileId fileId);

    File getFileMeta(AuthenticationToken token, FileId fileId);
}
