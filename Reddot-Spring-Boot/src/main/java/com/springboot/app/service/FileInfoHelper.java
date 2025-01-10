package com.springboot.app.service;

import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.UploadedFileData;
import com.springboot.app.forums.entity.FileInfo;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FileInfoHelper {

    @Autowired
    private FileService fileService;

    public List<FileInfo> createThumbnails(List<UploadedFileData> attachmentList) {

        List<FileInfo> fileInfos = new ArrayList<>();

        for (UploadedFileData uploadedFile : attachmentList) {
            FileInfo fileInfo = createThumbnail(uploadedFile);
            if (fileInfo != null) {
                fileInfos.add(fileInfo);
            }
        }

        return fileInfos;
    }

    public FileInfo createThumbnail(UploadedFileData uploadedFile) {
        // persist file content to disk
        ServiceResponse<String> uploadResponse =
                fileService.uploadCommentThumbnail(uploadedFile.getContents(),
                        FilenameUtils.getExtension(uploadedFile.getFileName()));

        if (uploadResponse.getAckCode() == AckCodeType.SUCCESS) {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setCreatedAt(uploadedFile.getUploadedDate());
            fileInfo.setContentType(uploadedFile.getContentType());
            fileInfo.setPath(uploadResponse.getDataObject());
            fileInfo.setName(uploadedFile.getOrigFileName());

            return fileInfo;
        }
        return null;
    }

    public List<FileInfo> createAttachments(List<UploadedFileData> attachmentList) {

        List<FileInfo> fileInfos = new ArrayList<FileInfo>();

        for (UploadedFileData uploadedFile : attachmentList) {
            FileInfo fileInfo = createAttachment(uploadedFile);
            if (fileInfo != null) {
                fileInfos.add(fileInfo);
            }
        }

        return fileInfos;
    }

    public FileInfo createAttachment(UploadedFileData uploadedFile) {
        // persist file content to disk
        ServiceResponse<String> uploadResponse =
                fileService.uploadCommentAttachment(uploadedFile.getContents(),
                        FilenameUtils.getExtension(uploadedFile.getFileName()));

        if (uploadResponse.getAckCode() == AckCodeType.SUCCESS) {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setCreatedAt(uploadedFile.getUploadedDate());
            fileInfo.setContentType(uploadedFile.getContentType());
            fileInfo.setPath(uploadResponse.getDataObject());
            fileInfo.setName(uploadedFile.getOrigFileName());

            return fileInfo;
        }

        return null;
    }

}