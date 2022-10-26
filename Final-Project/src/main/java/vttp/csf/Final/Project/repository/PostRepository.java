package vttp.csf.Final.Project.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import vttp.csf.Final.Project.dto.PostRequest;
import vttp.csf.Final.Project.model.Post;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import static vttp.csf.Final.Project.repository.Queries.*;

@Repository
public class PostRepository {

    @Autowired
    private JdbcTemplate template;

    private final Logger logger = Logger.getLogger(PostRepository.class.getName());

    public Integer savePost(int user_id, PostRequest post) {
        logger.info("in PostRepo: savePost for userId:" + user_id);

        List<String> tagList = post.getTags();
        String tags = String.join(",", tagList);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(SQL_INSERT_POST,
                    Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, post.getPostName());
            ps.setString(2, post.getDescription());
            ps.setString(3, tags);
            ps.setInt(4, user_id);
            return ps;

        }, keyHolder);

        BigInteger bigInt = (BigInteger) keyHolder.getKey();
        return bigInt.intValue();
    }

    public Optional<Post> getPostById(Long id) {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_POST_BY_POST_ID, id);
        if (!rs.first()) {
            logger.warning(">>>> PostRepository: getPost: no data found");
            return Optional.empty();
        }
        else return Optional.of(Post.create(rs));
    }

    public Optional<List<Post>> getAllPosts() {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_ALL_POSTS);
        if (!rs.isBeforeFirst()) {
            logger.warning(">>>> PostRepository: getAllPosts: no data found");
            return Optional.empty();
        }
        else {
            List<Post> list = new ArrayList<>();
            while (rs.next()) {
                list.add(Post.create(rs));
            }
            return Optional.of(list);
        }
    }


}
