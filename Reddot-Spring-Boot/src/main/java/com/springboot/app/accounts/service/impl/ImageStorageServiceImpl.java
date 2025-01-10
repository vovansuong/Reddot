package com.springboot.app.accounts.service.impl;

import com.springboot.app.accounts.entity.AvatarOption;
import com.springboot.app.accounts.service.AvatarOptionService;
import com.springboot.app.accounts.service.StorageService;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ServiceResponse;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ImageStorageServiceImpl implements StorageService {
    private static final Logger logger = LoggerFactory.getLogger(ImageStorageServiceImpl.class);
    String pathFolder = "assets" + File.separator + "images";
    private final Path storageFolder = Paths.get(pathFolder);
    @Autowired
    private AvatarOptionService avatarOptionService;

    public ImageStorageServiceImpl() {
        try {
            if (!Files.exists(storageFolder)) {
                Files.createDirectories(storageFolder);
            }
//			Files.createDirectories(storageFolder);
        } catch (Exception e) {
            logger.error("Could not create the directory where the uploaded files will be stored.", e);
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    private boolean isImageFile(MultipartFile file) {
        String fileExtensionString = FilenameUtils.getExtension(file.getOriginalFilename());
        String[] allowedExtensions = {"jpg", "jpeg", "png"};
        return Arrays.asList(allowedExtensions).contains(fileExtensionString.trim().toLowerCase());
    }

    @Override
    public ServiceResponse<String> storeFile(MultipartFile file, String preFilename) {
        ServiceResponse<String> response = new ServiceResponse<>();
        try {
            if (file.isEmpty()) {
                logger.error("Failed to store empty file " + file.getOriginalFilename());
                response.setAckCode(AckCodeType.FAILURE);
                response.addMessage("Failed to store empty file " + file.getOriginalFilename());
                return response;
            }
            // check if the file is an image
            if (!isImageFile(file)) {
                logger.error("Failed to store file " + file.getOriginalFilename() + ". Only image files are allowed.");
                response.setAckCode(AckCodeType.FAILURE);
                response.addMessage("Failed to store file " + file.getOriginalFilename() + ". Only image files are allowed.");
                return response;
            }
            AvatarOption avatarOption = avatarOptionService.getAvatarOption();
            // file must be less than 5 MB
            float fileSize = file.getSize() / 1_000_000.0f;
            if (fileSize > avatarOption.getMaxFileSize()) {
                logger.error("Failed to store file " + file.getOriginalFilename() + ". File size must be less than "
                             + avatarOption.getMaxFileSize() + " MB.");
                response.setAckCode(AckCodeType.FAILURE);
                response.addMessage("Failed to store file "
                                    + file.getOriginalFilename()
                                    + ". File size must be less than "
                                    + avatarOption.getMaxFileSize() + " MB.");
            }
            //check width and height of file image
            logger.info("File Content Type: " + file.getContentType());
            if (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/jpg") || file.getContentType().equals("image/png")) {
                BufferedImage image = ImageIO.read(file.getInputStream());
                if (image.getWidth() > avatarOption.getMaxWidth()
                    || image.getHeight() > avatarOption.getMaxHeight()) {
                    logger.error("Failed to store file " + file.getOriginalFilename() + ". Image size must be less than "
                                 + avatarOption.getMaxWidth() + "x" + avatarOption.getMaxHeight() + " pixels.");
                    response.setAckCode(AckCodeType.FAILURE);
                    response.addMessage("Failed to store file "
                                        + file.getOriginalFilename()
                                        + ". Image size must be less than "
                                        + avatarOption.getMaxWidth() + "x" + avatarOption.getMaxHeight() + " pixels.");
                    return response;
                }
            }

            // File must be renamed to avoid file name conflicts
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFilename = UUID.randomUUID().toString().replace("-", "");
            generatedFilename = preFilename + "_" + generatedFilename + "." + fileExtension;
            // Get the file path and normalize it to avoid file name conflicts
            Path destionationFilePath = this.storageFolder.resolve(generatedFilename).normalize().toAbsolutePath();
            // Check if the files' directory exists
            logger.info("File Path: " + destionationFilePath);
            if (!destionationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                logger.error("Cannot store file outside current directory.");
                response.setAckCode(AckCodeType.FAILURE);
                response.addMessage("Cannot store file outside current directory.");
                return response;
            }
            // Copy the file to the destination directory
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destionationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            logger.info("Stored file " + file.getOriginalFilename() + " as " + generatedFilename);
            response.setAckCode(AckCodeType.SUCCESS);
            response.addMessage("Stored file " + file.getOriginalFilename() + " as " + generatedFilename);
            response.setDataObject(generatedFilename);
        } catch (IOException e) {
            logger.error("Could not store the file. Error: " + e.getMessage());
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        return response;
    }


    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.storageFolder, 1)
                    .filter(path -> !path.equals(this.storageFolder) && !path.toString().contains("._"))
                    .map(this.storageFolder::relativize);

        } catch (Exception e) {
            throw new RuntimeException("Failed to read stored files", e);
        }
    }

    @Override
    public byte[] readFileContent(String filename) {
        try {
            Path file = storageFolder.resolve(filename);
            org.springframework.core.io.Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                try (InputStream inputStream = resource.getInputStream()) {
                    return StreamUtils.copyToByteArray(inputStream);
                }
            } else {
                throw new RuntimeException("Could not read the file.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not read the file. Error: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteFile(String filename) {
        try {
//			Path file = storageFolder.resolve(filename);
//			return Files.deleteIfExists(file);
            File avatarFile = storageFolder.resolve(filename).toFile();
            if (avatarFile.exists()) {
                avatarFile.delete();
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not delete the file. Error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void deleteAll() {
        try {
            Files.walk(this.storageFolder, 1).filter(path -> !path.equals(this.storageFolder)).map(Path::toFile)
                    .forEach(file -> {
                        if (!file.delete()) {
                            throw new RuntimeException("Could not delete the file " + file.getName());
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException("Could not delete the files. Error: " + e.getMessage());
        }
    }
}
