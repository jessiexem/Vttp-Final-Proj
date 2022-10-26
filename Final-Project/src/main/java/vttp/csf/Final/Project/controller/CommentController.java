package vttp.csf.Final.Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vttp.csf.Final.Project.dto.CommentsDto;
import vttp.csf.Final.Project.service.CommentService;

import java.util.List;

@RestController
@RequestMapping(path="/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentSvc;

    @PostMapping
    public ResponseEntity<String> createComment(@RequestBody CommentsDto commentsDto) {
        commentSvc.saveComment(commentsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Comment saved successfully");
    }

    @GetMapping
    public ResponseEntity<List<CommentsDto>> getAllCommentByPostId(
            @RequestParam (name="pid", required = false) Long postId) {
        List<CommentsDto> commentsDtoList = commentSvc.getAllCommentsByPost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(commentsDtoList);
    }


}
