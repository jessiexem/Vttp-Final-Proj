package vttp.csf.Final.Project.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
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
}
