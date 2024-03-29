package pl.jdacewicz.socialmediaserver.filestorage;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.socialmediaserver.filestorage.dto.*;

import java.io.IOException;

@RequiredArgsConstructor
public class FileStorageFacade {

    private final ImageValidator imageValidator;

    public FileNameGenerationResult generateFilename(MultipartFile file) {
        var newFileName = FileNameGenerator.generateFileName(file.getOriginalFilename());
        return FileNameGenerationResult.builder()
                .fileName(newFileName)
                .build();
    }

    public void uploadImage(MultipartFile image, FileUploadRequest fileUploadRequest) throws IOException {
        checkImageValidation(image);
        FileStorage.uploadFile(image, fileUploadRequest.fileName(), fileUploadRequest.fileUploadDirectory());
    }

    public void deleteFile(FileDeleteRequest fileDeleteRequest) throws IOException {
        FileStorage.deleteFile(fileDeleteRequest.fileName(), fileDeleteRequest.folderDirectory());
    }

    public void deleteDirectory(DirectoryDeleteRequest directoryDeleteRequest) throws IOException {
        FileStorage.deleteDirectory(directoryDeleteRequest.directory());
    }

    private void checkImageValidation(MultipartFile image) {
        var fileValidationResult = imageValidator.validate(image);
        if (fileValidationResult.doesValidationFailed()) {
            throw new ValidationException(fileValidationResult.validationMessage());
        }
    }
}
