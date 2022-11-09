package vttp.csf.Final.Project.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import vttp.csf.Final.Project.exception.HomeworkNerdException;
import vttp.csf.Final.Project.model.Comment;
import vttp.csf.Final.Project.model.Post;
import vttp.csf.Final.Project.model.User;
import vttp.csf.Final.Project.model.Vote;

import java.util.Optional;
import java.util.logging.Logger;

import static vttp.csf.Final.Project.repository.Queries.*;

@Repository
public class VoteRepository {

    @Autowired
    private JdbcTemplate template;

    private final Logger logger = Logger.getLogger(VoteRepository.class.getName());

    public boolean saveVote(Vote vote) {
        int added = template.update(SQL_INSERT_VOTE,
                vote.getVoteType().getDirection(),
                vote.getUser().getUserId(),
                vote.getComment().getId());

        return added>0;
    }

    public Optional<Vote> findMostRecentVoteByUserAndComment(Comment comment, User user) {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_MOST_RECENT_VOTE_BY_USER_AND_COMMENT,
                user.getUserId(),
                comment.getId());
        if (!rs.first()) {
            logger.warning(">>>> PostRepository: getPost: no data found");
            return Optional.empty();
        }
        else return Optional.of(Vote.create(rs));
    }

    public boolean deleteVotesByPostId(Long postId) {

        //check if the post has any comments
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_ALL_COMMENTS_BY_POST_ID,postId);
        if (!rs.isBeforeFirst()) {
            //check if comments has any votes based on postId
            if (countVotesByPostId(postId) >0) {
                int isDeleted = template.update(SQL_DELETE_VOTES_BY_POST_ID, postId);
                return isDeleted>0;
            }
        }
        return true;
    }

    public boolean deleteVotesByCommentId(int commentId) {
        if(countVotesByCommentId(commentId)>0) {
            int isDeleted = template.update(SQL_DELETE_VOTES_BY_COMMENT_ID, commentId);
            return isDeleted>0;
        }
        else {
            return true;
        }
    }

    public int countVotesByCommentId(int commentId) {
        final SqlRowSet rs = template.queryForRowSet(SQL_COUNT_VOTES_BY_COMMENT_ID, commentId);

        if(!rs.next()) {
            throw new HomeworkNerdException("unable to count votes By Comment Id");
        }
        return rs.getInt("count");
    }

    public int countVotesByPostId(Long postId){
        SqlRowSet rs = template.queryForRowSet(SQL_COUNT_VOTES_BY_POST_ID, postId);
        if(!rs.next()) {
            throw new HomeworkNerdException("unable to count votes By Comment Id");
        }
        return rs.getInt("count");
    }


}
