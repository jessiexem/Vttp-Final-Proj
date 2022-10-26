package vttp.csf.Final.Project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import vttp.csf.Final.Project.dto.CommentsDto;
import vttp.csf.Final.Project.model.Comment;
import vttp.csf.Final.Project.model.Post;
import vttp.csf.Final.Project.model.User;
import vttp.csf.Final.Project.model.VoteType;
import vttp.csf.Final.Project.repository.CommentRepository;
import vttp.csf.Final.Project.service.AuthService;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private AuthService authSvc;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentsDto.text")
    @Mapping(target = "createdDate", expression = "java(java.util.Date.from(java.time.Instant.now()))")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "voteCount", constant = "0")
    public abstract Comment mapDtoToComment (CommentsDto commentsDto, Post post, User user);

    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())" )
    @Mapping(target = "username", expression = "java(comment.getUser().getUsername())")
    //@Mapping(target = "commentCount", expression = "java(commentCount(comment))")
        //@Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
        //@Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
    public abstract CommentsDto mapCommentToDto (Comment comment);



}
