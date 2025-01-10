package com.springboot.app.forums.controller;

import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ObjectResponse;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.forums.dto.CommentDTO;
import com.springboot.app.forums.dto.search.SearchAll;
import com.springboot.app.forums.repository.CommentRepository;
import com.springboot.app.forums.service.CommentService;
import com.springboot.app.forums.service.DiscussionService;
import com.springboot.app.service.GenericService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/view/comments")
public class CommentViewController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private GenericService genericService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @GetMapping("/all")
    public ResponseEntity<ObjectResponse> getAllComments() {
        ServiceResponse<List<CommentDTO>> response = commentService.getAllComment();
        if (response.getAckCode() == AckCodeType.SUCCESS) {
            return ResponseEntity.ok(new ObjectResponse("200", "Get all comments successfully", response.getDataObject()));
        }
        return ResponseEntity.ok(new ObjectResponse("400", "Could not get all comments", null));
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<ObjectResponse> getCommentSearch(@PathVariable("keyword") String keyword) {
        ServiceResponse<List<SearchAll>> response = commentService.getSearchComments(keyword);
        if (response.getAckCode() == AckCodeType.SUCCESS) {
            return ResponseEntity.ok(new ObjectResponse("200", "Get comment search successfully", response.getDataObject()));
        }
        return ResponseEntity.ok(new ObjectResponse("400", "Could not get comment search", null));
    }

}
