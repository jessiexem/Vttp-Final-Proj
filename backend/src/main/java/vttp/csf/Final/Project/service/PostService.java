package vttp.csf.Final.Project.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vttp.csf.Final.Project.dto.PostRequest;
import vttp.csf.Final.Project.dto.PostResponse;
import vttp.csf.Final.Project.exception.HomeworkNerdException;
import vttp.csf.Final.Project.exception.PostNotFoundException;
import vttp.csf.Final.Project.mapper.PostMapper;
import vttp.csf.Final.Project.model.Post;
import vttp.csf.Final.Project.model.Topics;
import vttp.csf.Final.Project.model.User;
import vttp.csf.Final.Project.repository.CommentRepository;
import vttp.csf.Final.Project.repository.FavouriteRepository;
import vttp.csf.Final.Project.repository.PostRepository;
import vttp.csf.Final.Project.repository.VoteRepository;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private AuthService authSvc;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private AmazonS3 s3;

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private VoteRepository voteRepo;

    @Autowired
    private FavouriteRepository favRepo;

    private final Logger logger = Logger.getLogger(PostService.class.getName());

    @Transactional
    public Integer savePost(PostRequest postRequest) {
        User user = authSvc.getCurrentUser();
        Integer postId = postRepo.savePost(user.getUserId(), postRequest);
        return postId;
    }

    public String uploadToS3(MultipartFile file) {
        Map<String,String> myData = new HashMap<>();
        myData.put("createdOn", new Date().toString());

        //Metadata for the object
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        metadata.setUserMetadata(myData);

        String hash = UUID.randomUUID().toString().substring(0,8);

        try {
            PutObjectRequest putReq = new PutObjectRequest("jgclass",
                    "day38/%s".formatted(hash), file.getInputStream(), metadata);

            putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);
            PutObjectResult result = s3.putObject(putReq);
            return hash;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public PostResponse getPostById(Long id) {

        Optional<Post> optPost = postRepo.getPostById(id);
        if (optPost.isEmpty()) {
            throw new HomeworkNerdException("Invalid post id");
        }
        else {
            Post post = optPost.get();
            //map to PostResponse
            return postMapper.mapPostToDto(post);
        }
    }

    public List<PostResponse> getAllPosts(String searchTerm) {
        Optional<List<Post>> optList = postRepo.getAllPosts(searchTerm);
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

    public List<PostResponse> getAllPostsByUserId(int userId) {
        Optional<List<Post>> optList = postRepo.getAllPostsByUserId(userId);
        if (optList.isEmpty()) {
            return null;
        } else {
            List<Post> postList = optList.get();
            List<PostResponse> postResponseList =
                    postList.stream()
                            .map(p -> postMapper.mapPostToDto(p))
                            .collect(Collectors.toList());

            return postResponseList;
        }
    }

    @Transactional(rollbackFor = HomeworkNerdException.class)
    public void deletePostByUserByPostId(Long postId) {

        boolean isVotesDeleted = voteRepo.deleteVotesByPostId(postId);

        boolean isCommentsDeleted = commentRepo.deleteCommentsByPostId(postId);

        boolean isFavDeleted = favRepo.deleteFavouriteByPostId(postId);

        Optional<Post> optPost = postRepo.getPostById(postId);
        if (optPost.isEmpty()) {
            throw new HomeworkNerdException("Invalid post id");
        }
        else {
            String imageUrl = optPost.get().getImageUrl();
            if (imageUrl!=null) {
                deleteFileFromS3(imageUrl);
            }
        }

        boolean isPostDeleted = postRepo.deletePostByPostId(postId);

        if(!isVotesDeleted || !isCommentsDeleted || !isPostDeleted || !isFavDeleted) {
            throw new HomeworkNerdException("deletePostByUserByPostId: Unable to delete post by user");
        }
    }

    public void deleteFileFromS3(final String fileName) {
        s3.deleteObject("jgclass","day38/%s".formatted(fileName));
    }

    public Topics getAllTopics() {
        return postRepo.getAllTags();
    }
}
