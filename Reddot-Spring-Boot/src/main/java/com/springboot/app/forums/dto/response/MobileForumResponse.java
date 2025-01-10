package com.springboot.app.forums.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class MobileForumResponse {
    private Long id; //forum id
    private String title; //forum title
    private Long groupId;//forum group id
    private String groupName; //forum group name
    //discussion
    private List<MobileDiscussionResponse> discussions;
    private int totalComments;
}


