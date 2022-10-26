package vttp.csf.Final.Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vttp.csf.Final.Project.dto.PostRequest;
import vttp.csf.Final.Project.dto.PostResponse;
import vttp.csf.Final.Project.model.Post;
import vttp.csf.Final.Project.service.PostService;

import java.util.List;

@RestController
@RequestMapping(path="/api/posts/")
public class PostController {

    @Autowired
    private PostService postSvc;

    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody PostRequest postRequest) {
        Integer postId = postSvc.savePost(postRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Post %d has been saved successfully.", postId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        PostResponse postResponse = postSvc.getPostById(id);
        return ResponseEntity.status(HttpStatus.OK).body(postResponse);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> postResponseList = postSvc.getAllPosts();
        return ResponseEntity.status(HttpStatus.OK).body(postResponseList);
    }


}
