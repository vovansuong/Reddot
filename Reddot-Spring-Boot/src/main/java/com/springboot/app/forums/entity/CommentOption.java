package com.springboot.app.forums.entity;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comment_option")
public class CommentOption extends BaseEntity {
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
}
