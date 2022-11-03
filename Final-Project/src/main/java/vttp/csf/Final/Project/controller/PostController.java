package vttp.csf.Final.Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vttp.csf.Final.Project.dto.PostRequest;
import vttp.csf.Final.Project.dto.PostResponse;
import vttp.csf.Final.Project.model.Post;
import vttp.csf.Final.Project.service.PostService;

import java.util.List;

@RestController
@RequestMapping(path="/api/posts")
public class PostController {

    @Autowired
    private PostService postSvc;

    @PostMapping (consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createPost(@RequestPart String postName,
                                             @RequestPart String description,
                                             @RequestPart String tags,
                                             @RequestPart (required = false) MultipartFile file) {
        PostRequest postRequest = new PostRequest();
        postRequest.setPostName(postName);
        postRequest.setTags(tags);
        postRequest.setDescription(description);

        String fileHash = null;
        if (file != null) {
            fileHash = postSvc.uploadToS3(file);
        }
        if (fileHash !=null) {
            postRequest.setImageUrl(fileHash);
        }
        Integer postId = postSvc.savePost(postRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Post %d has been saved successfully.", postId));
    }

//    @PostMapping
//    public ResponseEntity<String> createPost(@RequestBody PostRequest postRequest) {
//        Integer postId = postSvc.savePost(postRequest);
//        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Post %d has been saved successfully.", postId));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        PostResponse postResponse = postSvc.getPostById(id);
        return ResponseEntity.status(HttpStatus.OK).body(postResponse);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam (required = false) String searchTerm) {
        if (searchTerm == null) {
            searchTerm = "";
        }
        List<PostResponse> postResponseList = postSvc.getAllPosts(searchTerm);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseList);
    }


}
