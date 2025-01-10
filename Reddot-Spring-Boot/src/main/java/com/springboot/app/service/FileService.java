package com.springboot.app.service;

import com.springboot.app.accounts.entity.AvatarOption;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ServiceResponse;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    private static final String DEFAULT_FILE_UPLOAD_DIRECTORY = System.getProperty("user.home")
                                                                + File.separator + "TechForum" + File.separator + "files";

    private static final String DEFAULT_AVATAR_FOLDER_PATH = "avatars";

    private static final String DEFAULT_COMMENT_THUMBNAIL_FOLDER_PATH =
            "comment" + File.separator + "thumbnails";

    private static final String DEFAULT_COMMENT_ATTACHMENT_FOLDER_PATH =
            "comment" + File.separator + "attachments";

    private static final String DEFAULT_MESSAGE_ATTACHMENT_FOLDER_PATH =
            "comment" + File.separator + "attachments";

    /* timestamp pattern used to generated unique file name for new file uploaded */
    private static final String TIMESTAMP_PATTERN = "yyyyMMdd.HHmmss.SSS";

    @Value("${File.uploadDirectory:undefined}")
    private String fileUploadDirectory;

    @Value("${Avatar.folderPath:#{null}}")
    private String avatarFolderPath;

    private Path avatarPath;

    @Value("${Avatar.imageType}")
    private String avatarImageType;

    @Value("${Comment.thumbnail.folderPath:#{null}}")
    private String commentThumbnailFolderPath;

    /* complete path to the comment thumbnail folder */
    private Path commentThumbnailPath;

    @Value("${Comment.attachment.folderPath:#{null}}")
    private String commentAttachmentFolderPath;

    /* complete path to the comment attachment folder */
    private Path commentAttachmentPath;

    @Value("#{ ${Message.attachment.maxFileSize} * 1024}")
    private Long messageAttachmentMaxSize;

    @Value("${Message.attachment.folderPath:#{null}}")
    private String messageAttachmentFolderPath;

    /* complete path to the message attachment folder */
    private Path messageAttachmentPath;

    private DateFormat dateFormat;

    @Autowired
    private SystemConfigService systemConfigService;


    @PostConstruct
    public void init() {
        logger.info("FileService initialized");
        if ("undefined".equals(fileUploadDirectory)) {
            fileUploadDirectory = DEFAULT_FILE_UPLOAD_DIRECTORY;
        }

        if (avatarFolderPath == null) {
            avatarFolderPath = DEFAULT_AVATAR_FOLDER_PATH;
        }

        avatarPath = Path.of(fileUploadDirectory, avatarFolderPath);
        // create avatar folder if not exist
        if (!avatarPath.toFile().exists()) {
            avatarPath.toFile().mkdirs();
        }

        if (commentThumbnailFolderPath == null) {
            commentThumbnailFolderPath = DEFAULT_COMMENT_THUMBNAIL_FOLDER_PATH;
        }

        commentThumbnailPath = Path.of(fileUploadDirectory, commentThumbnailFolderPath);
        // create comment thumbnail folder if not exist
        if (!commentThumbnailPath.toFile().exists()) {
            commentThumbnailPath.toFile().mkdirs();
        }

        if (commentAttachmentFolderPath == null) {
            commentAttachmentFolderPath = DEFAULT_COMMENT_ATTACHMENT_FOLDER_PATH;
        }

        commentAttachmentPath = Path.of(fileUploadDirectory, commentAttachmentFolderPath);
        // create comment attachment folder if not exist
        if (!commentAttachmentPath.toFile().exists()) {
            commentAttachmentPath.toFile().mkdirs();
        }

        if (messageAttachmentFolderPath == null) {
            messageAttachmentFolderPath = DEFAULT_MESSAGE_ATTACHMENT_FOLDER_PATH;
        }

        messageAttachmentPath = Path.of(fileUploadDirectory, messageAttachmentFolderPath);
        // create message attachment folder if not exist
        if (!messageAttachmentPath.toFile().exists()) {
            messageAttachmentPath.toFile().mkdirs();
        }

        dateFormat = new SimpleDateFormat(TIMESTAMP_PATTERN);

    }

    public ServiceResponse<String> uploadAvatar(byte[] bytes, String username) {
        ServiceResponse<String> response = new ServiceResponse<>();

        String fileName = username + "." + avatarImageType;
        File avatarFile = avatarPath.resolve(fileName).toFile();

        AvatarOption avatarOption = systemConfigService.getAvatarOption().getDataObject();
        if (avatarOption != null && avatarOption.getMaxFileSize() < bytes.length) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("File size exceeds maximum allowed size. Max size allowed: " + avatarOption.getMaxFileSize());
            return response;
        }

        try {
            BufferedImage bufferedImage = ImageIO.read(new java.io.ByteArrayInputStream(bytes));
            if (bufferedImage == null) {
                response.setAckCode(AckCodeType.FAILURE);
                response.addMessage("Invalid image file");
                return response;
            }

            assert avatarOption != null;
            if (bufferedImage.getWidth() > avatarOption.getMaxWidth() ||
                bufferedImage.getHeight() > avatarOption.getMaxHeight()) {
                response.setAckCode(AckCodeType.FAILURE);
                response.addMessage("Image size exceeds maximum allowed size. Max size allowed: " + avatarOption.getMaxWidth() + "x" + avatarOption.getMaxHeight());
                return response;
            }

            ImageIO.write(bufferedImage, avatarImageType, avatarFile);
            logger.info("Successfully uploaded avatar: " + fileName);

            response.setDataObject(fileName);
        } catch (Exception e) {
            logger.error("Error uploading avatar", e);
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Error uploading avatar");
        }

        return response;
    }


    public ServiceResponse<String> uploadAvatar(InputStream inputStream, long size, String username) {
        ServiceResponse<String> response = new ServiceResponse<>();
        AvatarOption avatarOption = systemConfigService.getAvatarOption().getDataObject();

        if (size > avatarOption.getMaxFileSize()) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("File size exceeds maximum allowed size. Max size allowed: " + avatarOption.getMaxFileSize());
            return response;
        }

        String fileName = username + "." + avatarImageType;
        File avatarFile = avatarPath.resolve(fileName).toFile();
        try {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            if (bufferedImage == null) {
                response.setAckCode(AckCodeType.FAILURE);
                response.addMessage("Invalid image file");
                return response;
            }
            if (bufferedImage.getWidth() > avatarOption.getMaxWidth() ||
                bufferedImage.getHeight() > avatarOption.getMaxHeight()) {
                response.setAckCode(AckCodeType.FAILURE);
                response.addMessage("Image size exceeds maximum allowed size. Max size allowed: " + avatarOption.getMaxWidth() + "x" + avatarOption.getMaxHeight());
                return response;
            }

            ImageIO.write(bufferedImage, avatarImageType, avatarFile);
            logger.info("Successfully uploaded avatar: " + fileName);

            response.setDataObject(fileName);
        } catch (Exception e) {
            logger.error("Error uploading avatar", e);
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Error uploading avatar");
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        return response;
    }

    public ServiceResponse<File> getAvatar(String username) {
        ServiceResponse<File> response = new ServiceResponse<>();
        File avatarFile = avatarPath.resolve(username + "." + avatarImageType).toFile();
        if (avatarFile.exists()) {
            response.setDataObject(avatarFile);
        } else {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Avatar not found for user: " + username);
        }
        return response;
    }

    public ServiceResponse<Boolean> deleteAvatar(String username) {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        File avatarFile = avatarPath.resolve(username + "." + avatarImageType).toFile();
        if (avatarFile.exists()) {
            if (avatarFile.delete()) {
                response.setDataObject(true);
            } else {
                response.setAckCode(AckCodeType.FAILURE);
                response.addMessage("Error deleting avatar for user: " + username);
            }
        } else {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Avatar not found for user: " + username);
        }
        return response;
    }

    public ServiceResponse<String> uploadCommentThumbnail(byte[] bytes, String extension) {
        ServiceResponse<String> response = new ServiceResponse<>();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        String fileName = createFileName(extension);
        File thumbnailFile = commentThumbnailPath.resolve(fileName).toFile();
        try {
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            if (bufferedImage == null) {
                response.setAckCode(AckCodeType.FAILURE);
                response.addMessage("Invalid image file");
                return response;
            }
            ImageIO.write(bufferedImage, extension, thumbnailFile);
            logger.info("Successfully uploaded comment thumbnail: " + fileName);
            response.setDataObject(fileName);
        } catch (Exception e) {
            logger.error("Error uploading comment thumbnail", e);
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Error uploading comment thumbnail");
        } finally {
            IOUtils.closeQuietly(byteArrayInputStream);
        }
        return response;
    }

    public ServiceResponse<File> getCommentThumbnail(String fileName) {
        ServiceResponse<File> response = new ServiceResponse<>();
        File thumbnailFile = commentThumbnailPath.resolve(fileName).toFile();
        if (thumbnailFile.exists()) {
            response.setDataObject(thumbnailFile);
        } else {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Comment thumbnail not found: " + fileName);
        }
        return response;

    }

    public ServiceResponse<Boolean> deleteCommentThumbnail(String fileName) {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        File thumbnailFile = commentThumbnailPath.resolve(fileName).toFile();
        if (thumbnailFile.exists()) {
            if (thumbnailFile.delete()) {
                response.setDataObject(true);
            } else {
                response.setAckCode(AckCodeType.FAILURE);
                response.addMessage("Error deleting comment thumbnail: " + fileName);
            }
        } else {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Comment thumbnail not found: " + fileName);
        }
        return response;
    }

    public ServiceResponse<String> uploadCommentAttachment(byte[] bytes, String extension) {
        ServiceResponse<String> response = new ServiceResponse<>();

        String fileName = createFileName(extension);
        File attachmentFile = commentAttachmentPath.resolve(fileName).toFile();
        try {
            FileUtils.writeByteArrayToFile(attachmentFile, bytes);
            logger.info("Successfully uploaded comment attachment: " + fileName);
            response.setDataObject(fileName);
        } catch (Exception e) {
            logger.error("Error uploading comment attachment", e);
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Error uploading comment attachment");
        }
        return response;

    }

    public ServiceResponse<File> getCommentAttachment(String fileName) {
        ServiceResponse<File> response = new ServiceResponse<>();
        File attachmentFile = commentAttachmentPath.resolve(fileName).toFile();
        if (attachmentFile.exists()) {
            response.setDataObject(attachmentFile);
        } else {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Comment attachment not found: " + fileName);
        }
        return response;
    }

    public ServiceResponse<Boolean> deleteCommentAttachment(String fileName) {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        File attachmentFile = commentAttachmentPath.resolve(fileName).toFile();
        if (attachmentFile.exists()) {
            if (attachmentFile.delete()) {
                response.setDataObject(true);
            } else {
                response.setAckCode(AckCodeType.FAILURE);
                response.addMessage("Error deleting comment attachment: " + fileName);
            }
        } else {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Comment attachment not found: " + fileName);
        }
        return response;
    }

    public ServiceResponse<String> uploadMessageAttachment(byte[] bytes, String extension) {
        ServiceResponse<String> response = new ServiceResponse<>();

        if (bytes.length > this.messageAttachmentMaxSize) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("File size exceeds maximum allowed size. Max size allowed: " + this.messageAttachmentMaxSize);
            return response;
        }

        String fileName = createFileName(extension);
        File attachmentFile = messageAttachmentPath.resolve(fileName).toFile();
        try {
            FileUtils.writeByteArrayToFile(attachmentFile, bytes);
            logger.info("Successfully uploaded message attachment: " + fileName);
            response.setDataObject(fileName);
        } catch (Exception e) {
            logger.error("Error uploading message attachment", e);
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Error uploading message attachment");
        }
        return response;
    }

    public ServiceResponse<File> getMessageAttachment(String fileName) {
        ServiceResponse<File> response = new ServiceResponse<>();
        File attachmentFile = messageAttachmentPath.resolve(fileName).toFile();
        if (attachmentFile.exists()) {
            response.setDataObject(attachmentFile);
        } else {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Message attachment not found: " + fileName);
        }
        return response;
    }

    public ServiceResponse<Boolean> deleteMessageAttachment(String fileName) {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        File attachmentFile = messageAttachmentPath.resolve(fileName).toFile();
        if (attachmentFile.exists()) {
            if (attachmentFile.delete()) {
                response.setDataObject(true);
            } else {
                response.setAckCode(AckCodeType.FAILURE);
                response.addMessage("Error deleting message attachment: " + fileName);
            }
        } else {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Message attachment not found: " + fileName);
        }
        return response;
    }


    private String createFileName(String extension) {
        String timestamp = dateFormat.format(LocalDateTime.now()) + "." + UUID.randomUUID().toString();
        return timestamp + "." + extension;
    }

}
