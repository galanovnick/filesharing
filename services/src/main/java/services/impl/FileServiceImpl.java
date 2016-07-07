package services.impl;

import entity.File;
import repository.FileRepository;
import services.AuthenticationException;
import services.FileService;
import services.UserAuthenticationService;

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

}
