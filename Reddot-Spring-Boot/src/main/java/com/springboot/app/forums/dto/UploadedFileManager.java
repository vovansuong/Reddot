package com.springboot.app.forums.dto;


/*
 * Helper class to manage uploaded files (upload/temporary store/delete)
 * before the user actually submit the complete form (with other data)
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class UploadedFileManager {
    private static final Logger logger = LoggerFactory.getLogger(UploadedFileManager.class);

    private int maxTotalFilesAllowed;
    private int maxTotalFileSizeAllowed;
    // this field is to keep track of selected uploadedFile (by UI user)
    private UploadedFileData selectedUploadedFile;
    // this field is to store (temporary) all uploadedFile (before user actually submit)
    private List<UploadedFileData> uploadedFileList = new ArrayList<UploadedFileData>();

    public UploadedFileManager(int maxFileCount, int maxFileSize) {
        this.maxTotalFilesAllowed = maxFileCount;
        this.maxTotalFileSizeAllowed = maxFileSize;
    }

    public UploadedFileData getSelectedUploadedFile() {
        return selectedUploadedFile;
    }

    public void setSelectedUploadedFile(UploadedFileData selectedUploadedFile) {
        this.selectedUploadedFile = selectedUploadedFile;
    }

    public List<UploadedFileData> getUploadedFileList() {
        return uploadedFileList;
    }

    public void setUploadedFileList(List<UploadedFileData> uploadedFileList) {
        this.uploadedFileList = uploadedFileList;
    }

//	public void addUploadedFile(UploadedFileData uploadedFile) {
//		if(isMaxTotalFilesExceeded()) {
//			logger.warn("max total files exceeded, cannot add more files");
//			return;
//		}
//
//		String tempFilename = LocalDateTime.now().toString()+"."+ FilenameUtils.getExtension(uploadedFile.getFileName());
//		uploadedFile.setFileName(tempFilename);
//		uploadedFileList.add(uploadedFile);
//	}

    public void removeUploadedFile(UploadedFileData uploadedFile) {
        uploadedFileList.remove(uploadedFile);
    }

    public void clearUploadedFiles() {
        uploadedFileList.clear();
    }

    public boolean isMaxTotalFilesExceeded() {
        return uploadedFileList.size() >= maxTotalFilesAllowed;
    }
}
