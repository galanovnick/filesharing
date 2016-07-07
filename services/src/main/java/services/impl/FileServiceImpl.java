package services.impl;

import com.google.common.base.Optional;
import entity.File;
import entity.tiny.FileId;
import entity.tiny.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.FileRepository;
import services.AuthenticationException;
import services.FileService;
import services.UserAuthenticationService;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of file management.
 */
public class FileServiceImpl implements FileService {

    private final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    private final FileRepository fileRepository;

    private final UserAuthenticationService authenticationService;

    public FileServiceImpl(FileRepository fileRepository, UserAuthenticationService authenticationService) {
        this.fileRepository = fileRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public void add(AuthenticationToken token,
                    File file, FileInputStream inputStream) throws AuthenticationException {

        checkNotNull(token, "Authentication token cannot be null");
        checkNotNull(file, "File cannot be null");
        checkNotNull(inputStream, "File input stream cannot be null");

        if (log.isDebugEnabled()) {
            log.debug("Adding file (filename = \"" + file.getName()
                    + "\") by user with id = \"" + file.getOwnerId() + "\"");
        }

        if (authenticationService.checkAuthentication(token, file.getOwnerId())) {
            fileRepository.add(file, inputStream);
        } else {
            throw new AuthenticationException("Specified token is not authenticate.");
        }
    }

    @Override
    public ByteArrayInputStream getFileContent(AuthenticationToken token, FileId fileId, UserId userId)
            throws AuthenticationException {

        checkNotNull(token, "Authentication token cannot be null");
        checkNotNull(fileId, "File id cannot be null");
        checkNotNull(userId, "User id cannot be null");

        if (log.isDebugEnabled()) {
            log.debug("Getting file content(file id = \""
                    + fileId.get() + "\" by user with id = \"" + userId + "\"");
        }

        if (authenticationService.checkAuthentication(token, userId)) {

            Optional<ByteArrayInputStream> fileContent = fileRepository.getFileContent(fileId);

            if (fileContent.isPresent()) {
                return fileContent.get();
            } else {
                throw new IllegalArgumentException("Invalid file id.");
            }
        } else {
            throw new AuthenticationException("Specified token is not authenticate.");
        }
    }

    @Override
    public File getFileMeta(AuthenticationToken token, FileId fileId, UserId userId) throws AuthenticationException {

        checkNotNull(token, "Authentication token cannot be null");
        checkNotNull(fileId, "File id cannot be null");
        checkNotNull(userId, "User id cannot be null");

        if (log.isDebugEnabled()) {
            log.debug("Getting file meta(file id = \""
                    + fileId.get() + "\" by user with id = \"" + userId + "\"");
        }

        if (authenticationService.checkAuthentication(token, userId)) {

            Optional<File> fileContent = fileRepository.getFileMeta(fileId);

            if (fileContent.isPresent()) {
                return fileContent.get();
            } else {
                throw new IllegalArgumentException("Invalid file id.");
            }
        } else {
            throw new AuthenticationException("Specified token is not authenticate.");
        }
    }

    @Override
    public void deleteFile(AuthenticationToken token, FileId fileId, UserId userId) {

        if (log.isDebugEnabled()) {
            log.debug("Trying remove file with id = \"" + fileId + "\" by user with id = \"" + userId + "\".");
        }

        Optional<File> fileMeta = fileRepository.getFileMeta(fileId);

        if (fileMeta.isPresent()) {
            if (!fileMeta.get().getOwnerId().equals(userId)) {
                throw new IllegalStateException("File can be removed only by owner.");
            }

            fileRepository.removeFile(fileId);

            if (log.isDebugEnabled()) {
                log.debug("File (fileId = \"" + fileMeta.get().getId().get() + "\" has been removed.");
            }
        } else {
            throw new IllegalArgumentException("Invalid file id.");
        }
    }
}
