package vttp.csf.Final.Project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vttp.csf.Final.Project.dto.VoteDto;
import vttp.csf.Final.Project.exception.HomeworkNerdException;
import vttp.csf.Final.Project.model.Comment;
import vttp.csf.Final.Project.model.User;
import vttp.csf.Final.Project.model.Vote;
import vttp.csf.Final.Project.model.VoteType;
import vttp.csf.Final.Project.repository.CommentRepository;
import vttp.csf.Final.Project.repository.VoteRepository;

import java.util.Optional;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepo;

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private AuthService authSvc;

    public void saveVote(VoteDto voteDto) {
        //check if comment exists
        Optional<Comment> optComment = commentRepo.getCommentByCommentId(voteDto.getCommentId());
        if (optComment.isEmpty()) {
            throw new HomeworkNerdException("VoteService - vote() : Comment does not exist");
        }
        else {
            Comment comment = optComment.get();
            User user = authSvc.getCurrentUser();

            //check if user has already voted the same vote recently before
            Optional<Vote> opt = voteRepo.findMostRecentVoteByUserAndComment(comment, user);
            if (opt.isPresent() && opt.get().getVoteType().equals(voteDto.getVoteType())) {
                throw new HomeworkNerdException("You have already "+ voteDto.getVoteType() + "d for this comment");
            }
            if (voteDto.getVoteType().equals(VoteType.UPVOTE)) {
                comment.setVoteCount(comment.getVoteCount()+1);
            } else {
                comment.setVoteCount(comment.getVoteCount()-1);
            }

            Vote currVote = mapDtoToVote(voteDto, comment);
            boolean voteSaved = voteRepo.saveVote(currVote);
            boolean commentUpdated = commentRepo.updateCommentVoteCount(comment);
            if(!(commentUpdated || voteSaved)) {
                throw new HomeworkNerdException("Vote count was not updated for comment "+comment.getId());
            }
        }
    }

    private Vote mapDtoToVote(VoteDto voteDto, Comment comment) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .user(authSvc.getCurrentUser())
                .comment(comment)
                .build();
    }
}
