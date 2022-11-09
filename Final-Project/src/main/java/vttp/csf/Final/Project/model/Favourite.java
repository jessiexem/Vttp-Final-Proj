package vttp.csf.Final.Project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import vttp.csf.Final.Project.dto.PostResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Favourite {

    private Integer recordId;
    private Integer userId;
    private Post post;

    public static Favourite create(SqlRowSet rs) {
        Favourite fav = new Favourite();
        fav.setRecordId(rs.getInt("record_id"));
        fav.setUserId(rs.getInt("fav_user_id"));
        Post post = Post.create(rs);
        fav.setPost(post);

        return fav;
    }
}
