package vttp.csf.Final.Project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import vttp.csf.Final.Project.dto.PostRequest;
import vttp.csf.Final.Project.dto.PostResponse;
import vttp.csf.Final.Project.exception.HomeworkNerdException;
import vttp.csf.Final.Project.exception.PostNotFoundException;
import vttp.csf.Final.Project.mapper.PostMapper;
import vttp.csf.Final.Project.model.Post;
import vttp.csf.Final.Project.model.User;
import vttp.csf.Final.Project.repository.PostRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private AuthService authSvc;

    @Autowired
    private PostMapper postMapper;

    @Transactional
    public Integer savePost(PostRequest postRequest) {
        User user = authSvc.getCurrentUser();
        Integer postId = postRepo.savePost(user.getUserId(), postRequest);
        return postId;
    }

    public PostResponse getPostById(Long id) {

        Optional<Post> optPost = postRepo.getPostById(id);
        if (optPost.isEmpty()) {
            throw new HomeworkNerdException("Invalid refresh token");
        }
        else {
            Post post = optPost.get();
            //map to PostResponse
            return postMapper.mapPostToDto(post);
        }
    }

    public List<PostResponse> getAllPosts() {
        Optional<List<Post>> optList = postRepo.getAllPosts();
        if (optList.isEmpty()) {
            throw new PostNotFoundException("No posts found");
        } else {
            List<Post> postList = optList.get();
            List<PostResponse> postResponseList =
                    postList.stream()
                            .map(p -> postMapper.mapPostToDto(p))
                            .collect(Collectors.toList());
            return postResponseList;
        }
    }
}
