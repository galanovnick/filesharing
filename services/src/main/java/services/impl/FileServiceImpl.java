package services.impl;

import entity.File;
import repository.FileRepository;
import services.FileService;

/**
 * Implementation of file management.
 */
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public void add(File file) {
        fileRepository.add(file);
    }
}
