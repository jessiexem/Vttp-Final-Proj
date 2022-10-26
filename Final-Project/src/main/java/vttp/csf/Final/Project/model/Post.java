package vttp.csf.Final.Project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    private Long postId;
    private String postName;
    //private String url;
    private String description;
    private User user;
    private Instant createdDate;
    private List<String> tags;
//    private Topic topic;

    public static Post create(SqlRowSet rs) {
        Post post = new Post();
        post.setPostId(rs.getLong("pid"));
        post.setPostName(rs.getString("post_name"));
        post.setDescription(rs.getString("description"));
        String tags = rs.getString("tags");
        List<String> tagList = new ArrayList<String>(Arrays.asList(tags.split(",")));
        post.setTags(tagList);
        //post.setCreatedDate(rs.getDate("p_created_date"));
        post.setCreatedDate(rs.getTimestamp("p_created_date").toInstant());
        post.setUser(User.create(rs));
        return post;
    }

}
