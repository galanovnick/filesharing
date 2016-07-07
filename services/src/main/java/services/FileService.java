package services;

import entity.File;
import entity.tiny.UserId;

/**
 * Interface for file management.
 */
public interface FileService {

    /**
     * Add file with specified user as owner.
     * @param file - file to be added.
     * @param ownerId - owner id
     */
    void add(File file, UserId ownerId);
}
