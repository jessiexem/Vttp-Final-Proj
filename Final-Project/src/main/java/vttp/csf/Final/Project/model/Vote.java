package vttp.csf.Final.Project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import vttp.csf.Final.Project.dto.VoteDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vote {

    private Long voteId;
    private VoteType voteType;
    private Comment comment;
    private User user;

    public static Vote create(SqlRowSet rs) {
        Vote vote = new Vote();
        vote.setVoteId(rs.getLong("vid"));
        vote.setVoteType(VoteType.lookup(rs.getInt("vote_type")));
        Comment comment = Comment.create(rs);
        vote.setComment(comment);
        User user = User.create(rs);
        vote.setUser(user);

        return vote;
    }
}
