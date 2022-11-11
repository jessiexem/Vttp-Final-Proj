package vttp.csf.Final.Project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vttp.csf.Final.Project.dto.CommentsDto;
import vttp.csf.Final.Project.dto.CommentsSummaryDto;
import vttp.csf.Final.Project.exception.HomeworkNerdException;
import vttp.csf.Final.Project.exception.PostNotFoundException;
import vttp.csf.Final.Project.mapper.CommentMapper;
import vttp.csf.Final.Project.model.Comment;
import vttp.csf.Final.Project.model.NotificationEmail;
import vttp.csf.Final.Project.model.Post;
import vttp.csf.Final.Project.model.User;
import vttp.csf.Final.Project.repository.CommentRepository;
import vttp.csf.Final.Project.repository.PostRepository;
import vttp.csf.Final.Project.repository.VoteRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private AuthService authSvc;

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private MailContentBuilder mailContentBuilder;

    @Autowired
    private MailService mailSvc;

    @Autowired
    private PostService postSvc;

    @Autowired
    private VoteRepository voteRepo;

    public void saveComment(CommentsDto commentsDto) {
        //check if post exists
        Optional<Post> optPost = postRepo.getPostById(commentsDto.getPostId());
        if (optPost.isEmpty()) {
            throw new PostNotFoundException(commentsDto.getPostId().toString());
        } else {
            Post post = optPost.get();
            Comment comment = commentMapper.mapDtoToComment(commentsDto, post, authSvc.getCurrentUser());
            boolean added = commentRepo.saveComment(comment);

            if(!added) {
                throw new HomeworkNerdException("Unable to save comment");
            }

            String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post:"+ comment.getText());
            sendCommentNotification(message, post.getUser());
        }
     }

    private void sendCommentNotification(String message, User user) {
        mailSvc.sendMail(new NotificationEmail(user.getUsername()+" commented on your post",
                user.getEmail(), message));
    }

    public List<CommentsDto> getAllCommentsByPost(Long postId) {
        //check if post exists
        Optional<Post> optPost = postRepo.getPostById(postId);
        if (optPost.isEmpty()) {
            throw new PostNotFoundException(postId.toString());
        } else {
            Post post = optPost.get();
            Optional<List<Comment>> optList = commentRepo.getAllCommentsByPost(post);
            if (optList.isEmpty()) {
                return null;
                //throw new HomeworkNerdException("Comments not found for postId: "+ post.getPostId());
            }
            List<Comment> comments = optList.get();
            List<CommentsDto> commentsDtoList =
                    comments.stream()
                            .map(c-> commentMapper.mapCommentToDto(c))
                            .collect(Collectors.toList());
            return commentsDtoList;
        }
    }

    public List<CommentsSummaryDto> getAllCommentsByUserId(int userId) {
        Optional<List<Comment>> optList = commentRepo.getAllCommentsByUserId(userId);
        if(optList.isEmpty()) {
            return null;
        } else {
            List<Comment> comments = optList.get();
            List<CommentsSummaryDto> commentsSummaryDtoList =
                    comments.stream()
                            .map(c-> commentMapper.mapCommentToSummaryDto(c))
                            .collect(Collectors.toList());
            return commentsSummaryDtoList;
        }
    }

    @Transactional(rollbackFor = HomeworkNerdException.class)
    public void deleteCommentByUserByCommentId(int cid) {
        boolean isVotesDeleted = voteRepo.deleteVotesByCommentId(cid);

        boolean isCommentsDeleted = commentRepo.deleteCommentsByCommentId(cid);

        if(!isVotesDeleted || !isCommentsDeleted) {
            throw new HomeworkNerdException("deleteCommentByUserByCommentId: Unable to delete comment by user");
        }
    }
}
