package com.springboot.app.forums.dto;

import java.time.LocalDateTime;

public class UploadedFileData {
    private String fileName;
    private String origFileName;
    private String contentType;
    /* if contents is NOT null, this instance is a newly (un-stored) file
     * this is useful when user is just upload the file, before actually submit
     * the complete form
     */
    private byte[] contents;
    private LocalDateTime uploadedDate;
    /*if fileInfoId is NOT null, this instance is connected to a stored file
     * this is useful in the cases where we need to delete an already stored file
     */
    private Long fileInfoId;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOrigFileName() {
        return origFileName;
    }

    public void setOrigFileName(String origFileName) {
        this.origFileName = origFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }

    public LocalDateTime getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(LocalDateTime uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public Long getFileInfoId() {
        return fileInfoId;
    }

    public void setFileInfoId(Long fileInfoId) {
        this.fileInfoId = fileInfoId;
    }
}
