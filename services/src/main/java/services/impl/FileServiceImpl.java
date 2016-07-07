package services.impl;

import com.google.common.base.Optional;
import entity.File;
import entity.tiny.FileId;
import repository.FileRepository;
import services.AuthenticationException;
import services.FileService;
import services.UserAuthenticationService;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;

/**
 * Implementation of file management.
 */
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    private final UserAuthenticationService authenticationService;

    public FileServiceImpl(FileRepository fileRepository, UserAuthenticationService authenticationService) {
        this.fileRepository = fileRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public void add(AuthenticationToken token,
                    File file, FileInputStream inputStream) throws AuthenticationException {

        if (authenticationService.checkAuthentication(token, file.getOwnerId())) {
            fileRepository.add(file, inputStream);
        } else {
            throw new AuthenticationException("Specified token is not authenticate.");
        }
    }

    @Override
    public ByteArrayInputStream getFileContent(AuthenticationToken token, File file)
            throws AuthenticationException {

        if (authenticationService.checkAuthentication(token, file.getOwnerId())) {

            Optional<ByteArrayInputStream> fileContent = fileRepository.getFileContent(file.getId());

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
    public File getFileMeta(AuthenticationToken token, File file) throws AuthenticationException {
        if (authenticationService.checkAuthentication(token, file.getOwnerId())) {

            Optional<File> fileMeta = fileRepository.getFileMeta(file.getId());

            if (fileMeta.isPresent()) {
                return fileMeta.get();
            } else {
                throw new IllegalArgumentException("Invalid file id.");
            }
        } else {
            throw new AuthenticationException("Specified token is not authenticate.");
        }
    }
}
