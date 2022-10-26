package vttp.csf.Final.Project.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import vttp.csf.Final.Project.model.Comment;
import vttp.csf.Final.Project.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static vttp.csf.Final.Project.repository.Queries.*;

@Repository
public class CommentRepository {

    @Autowired
    private JdbcTemplate template;

    private final Logger logger = Logger.getLogger(CommentRepository.class.getName());

    public boolean saveComment (Comment comment) {
        int added = template.update(SQL_INSERT_COMMENT,
                comment.getText(),
                comment.getVoteCount(),
                comment.getUser().getUserId(),
                comment.getPost().getPostId());

        return added==1;
    }

    public Optional<List<Comment>>getAllCommentsByPost(Post post) {
        final SqlRowSet rs = template.queryForRowSet(SQL_FIND_ALL_COMMENTS_BY_POST_ID,
                post.getPostId());
        if (!rs.isBeforeFirst()) {
            logger.warning(">>>> CommentRepo: findAllCommentsByPost: no data found");
            return Optional.empty();
        }
        else {
            List<Comment> list = new ArrayList<>();
            while (rs.next()) {
                list.add(Comment.create(rs));
            }
            return Optional.of(list);
         }
    }

    public Optional<Comment> getCommentByCommentId (Long cid) {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_COMMENT_BY_CID,
                cid);

        if(!rs.first()) {
            logger.warning(">>>> CommentRepo: getCommentByCommentId: no data found");
            return Optional.empty();
        }
        else return Optional.of(Comment.create(rs));
    }

    public boolean updateCommentVoteCount(Comment comment) {
        int updated = template.update(SQL_UPDATE_COMMENT_VOTE_COUNT,
                comment.getVoteCount(),
                comment.getId());

        return updated==1;
    }
}