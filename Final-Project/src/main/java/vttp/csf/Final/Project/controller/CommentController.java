package vttp.csf.Final.Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vttp.csf.Final.Project.dto.CommentsDto;
import vttp.csf.Final.Project.dto.CommentsSummaryDto;
import vttp.csf.Final.Project.exception.HomeworkNerdException;
import vttp.csf.Final.Project.model.User;
import vttp.csf.Final.Project.repository.UserRepository;
import vttp.csf.Final.Project.service.CommentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentSvc;

    @Autowired
    private UserRepository userRepo;

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

    @GetMapping("/profile/{username}")
    public ResponseEntity<List<CommentsSummaryDto>> getAllCommentsByUsername(@PathVariable (name="username") String username) {
        Optional<User> optUser = userRepo.findUserByUsername(username);
        if (optUser.isEmpty()) {
            throw new HomeworkNerdException("getAllPostsByUser: No such user");
        } else {
            User user = optUser.get();
            int uid = user.getUserId();
            List<CommentsSummaryDto> commentsSummaryDtoList = commentSvc.getAllCommentsByUserId(uid);
            return ResponseEntity.status(HttpStatus.OK).body(commentsSummaryDtoList);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePostByUserByPostId(@PathVariable (name="id") int cid) {
        commentSvc.deleteCommentByUserByCommentId(cid);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted comment with id:"+cid);
    }


}
