package com.springboot.app.forums.entity;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment_option")
public class CommentOption extends BaseEntity {
    @Id
    private Long id;
    @Column(name = "min_char_discussion_title")
    private int minCharDiscussionTitle;
    @Column(name = "max_char_discussion_title")
    private int maxCharDiscussionTitle;
    @Column(name = "min_char_discussion_content")
    private int minCharDiscussionContent;
    @Column(name = "max_char_discussion_content")
    private int maxCharDiscussionContent;
    @Column(name = "max_discussion_thumbnail")
    private int maxDiscussionThumbnail;
    @Column(name = "max_discussion_attachment")
    private int maxDiscussionAttachment;
    @Column(name = "max_byte_discussion_thumbnail")
    private int maxByteDiscussionThumbnail;
    @Column(name = "max_byte_discussion_attachment")
    private int maxByteDiscussionAttachment;
    @Column(name = "allow_discussion_title_edit")
    private boolean allowDiscussionTitleEdit;
    @Column(name = "min_char_comment_title")
    private int minCharCommentTitle;
    @Column(name = "max_char_comment_title")
    private int maxCharCommentTitle;
    @Column(name = "min_char_comment_content")
    private int minCharCommentContent;
    @Column(name = "max_char_comment_content")
    private int maxCharCommentContent;
    @Column(name = "max_comment_thumbnail")
    private int maxCommentThumbnail;
    @Column(name = "max_comment_attachment")
    private int maxCommentAttachment;
    @Column(name = "max_byte_comment_attachment")
    private int maxByteCommentAttachment;
    @Column(name = "max_byte_comment_thumbnail")
    private int maxByteCommentThumbnail;
    @Column(name = "allow_comment_edit")
    private boolean allowCommentEdit;
    @Column(name = "max_discussion_tags")
    private int maxDiscussionTags;

    @PrePersist
    public void prePersist() {
        this.setCreatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void preUpdate() {
        this.setUpdatedAt(LocalDateTime.now());
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMinCharDiscussionTitle() {
        return minCharDiscussionTitle;
    }

    public void setMinCharDiscussionTitle(int minCharDiscussionTitle) {
        this.minCharDiscussionTitle = minCharDiscussionTitle;
    }

    public int getMaxCharDiscussionTitle() {
        return maxCharDiscussionTitle;
    }

    public void setMaxCharDiscussionTitle(int maxCharDiscussionTitle) {
        this.maxCharDiscussionTitle = maxCharDiscussionTitle;
    }

    public int getMinCharDiscussionContent() {
        return minCharDiscussionContent;
    }

    public void setMinCharDiscussionContent(int minCharDiscussionContent) {
        this.minCharDiscussionContent = minCharDiscussionContent;
    }

    public int getMaxCharDiscussionContent() {
        return maxCharDiscussionContent;
    }

    public void setMaxCharDiscussionContent(int maxCharDiscussionContent) {
        this.maxCharDiscussionContent = maxCharDiscussionContent;
    }

    public int getMaxDiscussionThumbnail() {
        return maxDiscussionThumbnail;
    }

    public void setMaxDiscussionThumbnail(int maxDiscussionThumbnail) {
        this.maxDiscussionThumbnail = maxDiscussionThumbnail;
    }

    public int getMaxDiscussionAttachment() {
        return maxDiscussionAttachment;
    }

    public void setMaxDiscussionAttachment(int maxDiscussionAttachment) {
        this.maxDiscussionAttachment = maxDiscussionAttachment;
    }

    public int getMaxByteDiscussionThumbnail() {
        return maxByteDiscussionThumbnail;
    }

    public void setMaxByteDiscussionThumbnail(int maxByteDiscussionThumbnail) {
        this.maxByteDiscussionThumbnail = maxByteDiscussionThumbnail;
    }

    public int getMaxByteDiscussionAttachment() {
        return maxByteDiscussionAttachment;
    }

    public void setMaxByteDiscussionAttachment(int maxByteDiscussionAttachment) {
        this.maxByteDiscussionAttachment = maxByteDiscussionAttachment;
    }

    public boolean isAllowDiscussionTitleEdit() {
        return allowDiscussionTitleEdit;
    }

    public void setAllowDiscussionTitleEdit(boolean allowDiscussionTitleEdit) {
        this.allowDiscussionTitleEdit = allowDiscussionTitleEdit;
    }

    public int getMinCharCommentTitle() {
        return minCharCommentTitle;
    }

    public void setMinCharCommentTitle(int minCharCommentTitle) {
        this.minCharCommentTitle = minCharCommentTitle;
    }

    public int getMaxCharCommentTitle() {
        return maxCharCommentTitle;
    }

    public void setMaxCharCommentTitle(int maxCharCommentTitle) {
        this.maxCharCommentTitle = maxCharCommentTitle;
    }

    public int getMinCharCommentContent() {
        return minCharCommentContent;
    }

    public void setMinCharCommentContent(int minCharCommentContent) {
        this.minCharCommentContent = minCharCommentContent;
    }

    public int getMaxCharCommentContent() {
        return maxCharCommentContent;
    }

    public void setMaxCharCommentContent(int maxCharCommentContent) {
        this.maxCharCommentContent = maxCharCommentContent;
    }

    public int getMaxCommentThumbnail() {
        return maxCommentThumbnail;
    }

    public void setMaxCommentThumbnail(int maxCommentThumbnail) {
        this.maxCommentThumbnail = maxCommentThumbnail;
    }

    public int getMaxCommentAttachment() {
        return maxCommentAttachment;
    }

    public void setMaxCommentAttachment(int maxCommentAttachment) {
        this.maxCommentAttachment = maxCommentAttachment;
    }

    public int getMaxByteCommentAttachment() {
        return maxByteCommentAttachment;
    }

    public void setMaxByteCommentAttachment(int maxByteCommentAttachment) {
        this.maxByteCommentAttachment = maxByteCommentAttachment;
    }

    public int getMaxByteCommentThumbnail() {
        return maxByteCommentThumbnail;
    }

    public void setMaxByteCommentThumbnail(int maxByteCommentThumbnail) {
        this.maxByteCommentThumbnail = maxByteCommentThumbnail;
    }

    public boolean isAllowCommentEdit() {
        return allowCommentEdit;
    }

    public void setAllowCommentEdit(boolean allowCommentEdit) {
        this.allowCommentEdit = allowCommentEdit;
    }

    public int getMaxDiscussionTags() {
        return maxDiscussionTags;
    }

    public void setMaxDiscussionTags(int maxDiscussionTags) {
        this.maxDiscussionTags = maxDiscussionTags;
    }
}
