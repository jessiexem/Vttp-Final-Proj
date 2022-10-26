package vttp.csf.Final.Project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Long id;
    private String text;
    private Post post;
    private Date createdDate;
    private User user;
    private Integer voteCount = 0;

    public static Comment create(SqlRowSet rs) {
        Comment comment = new Comment();
        comment.setId(rs.getLong("cid"));
        comment.setText(rs.getString("text"));
        comment.setCreatedDate(rs.getDate("c_created_date"));
        comment.setVoteCount(rs.getInt("vote_count"));
        User user = User.create(rs);
        comment.setUser(user);
        Post post = Post.create(rs);
        comment.setPost(post);

        return comment;
    }
}
