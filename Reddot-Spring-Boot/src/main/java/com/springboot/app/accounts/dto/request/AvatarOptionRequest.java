package com.springboot.app.accounts.dto.request;

public class AvatarOptionRequest {
    private int maxFileSize;

    private int maxWidth;

    private int maxHeight;

    public AvatarOptionRequest() {
    }

    public AvatarOptionRequest(int maxFileSize, int maxWidth, int maxHeight) {
        this.maxFileSize = maxFileSize;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    public int getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(int maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }
}
