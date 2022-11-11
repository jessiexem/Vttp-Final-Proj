package vttp.csf.Final.Project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import vttp.csf.Final.Project.dto.PostRequest;
import vttp.csf.Final.Project.dto.PostResponse;
import vttp.csf.Final.Project.model.Comment;
import vttp.csf.Final.Project.model.Post;
import vttp.csf.Final.Project.model.User;
import vttp.csf.Final.Project.repository.CommentRepository;
import vttp.csf.Final.Project.service.AuthService;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private AuthService authSvc;

    @Autowired
    private CommentRepository commentRepo;

//    @Mapping(target = "postId", source = "postRequest.postId")
//    @Mapping(target = "postName", source = "postRequest.postName")
//    @Mapping(target = "description", source = "postRequest.description")
//    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
//    //@Mapping(target = "createdDate", expression = "java(java.util.Date.from(java.time.Instant.now()))")
//    @Mapping(target = "user", source = "user")
//    public abstract Post map (PostRequest postRequest, User user);


    @Mapping(target = "id", source = "post.postId")
    @Mapping(target = "userName", source = "post.user.username")
    @Mapping(target = "tags", source = "post.tags")
    @Mapping(target = "imageUrl", source = "post.imageUrl")
    @Mapping(target = "posterDpImageUrl", source = "post.user.dpImageUrl")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    public abstract PostResponse mapPostToDto (Post post);

    Integer commentCount(Post post) {
        Optional<List<Comment>> optList = commentRepo.getAllCommentsByPost(post);
        if (optList.isEmpty()) {
            return 0;
        } else {
            return optList.get().size();
        }
    }

}
