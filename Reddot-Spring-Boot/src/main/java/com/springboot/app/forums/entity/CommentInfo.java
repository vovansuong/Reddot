package com.springboot.app.forums.entity;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment_info")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentInfo extends BaseEntity {
    @Column(name = "title", length = 255)
    private String title;
    @Column(name = "content_abbr", length = 255)
    private String contentAbbr; // abbreviated content of the comment for display
    @Column(name = "comment_id")
    private Long commentId; // id of the comment
    @Column(name = "commenter")
    private String commenter;
    @Column(name = "comment_date", columnDefinition = "DATETIME")
    // columnDefinition = "DATETIME" is added to fix the error "Table 'discussions.comment_info' doesn't exist
    private LocalDateTime commentDate;
}
