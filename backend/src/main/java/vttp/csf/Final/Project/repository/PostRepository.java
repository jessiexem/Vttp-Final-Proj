package vttp.csf.Final.Project.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import vttp.csf.Final.Project.dto.PostRequest;
import vttp.csf.Final.Project.model.Post;
import vttp.csf.Final.Project.model.Topic;
import vttp.csf.Final.Project.model.Topics;

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

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(SQL_INSERT_POST,
                    Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, post.getPostName());
            ps.setString(2, post.getDescription());
            ps.setString(3, post.getTags());
            ps.setInt(4, user_id);
            ps.setString(5,post.getImageUrl());
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

    public Optional<List<Post>> getAllPosts(String searchTerm) {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_ALL_POSTS, searchTerm, searchTerm, searchTerm);
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

    public Optional<List<Post>> getAllPostsByUserId(int userId) {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_ALL_POSTS_BY_USER_ID, userId);
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

    public boolean deletePostByPostId(Long postId) {
        int isDeleted = template.update(SQL_DELETE_POST_BY_POST_ID, postId);
        return isDeleted>0;
    }

    public Topics getAllTags() {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_ALL_TAGS);
        if (!rs.isBeforeFirst()) {
            return null;
        } else {
            return Topics.create(rs);
        }
    }


}
