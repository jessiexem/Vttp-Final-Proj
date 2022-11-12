package vttp.csf.Final.Project.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import vttp.csf.Final.Project.exception.HomeworkNerdException;
import vttp.csf.Final.Project.model.Favourite;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static vttp.csf.Final.Project.repository.Queries.*;

@Repository
public class FavouriteRepository {

    @Autowired
    private JdbcTemplate template;

    private final Logger logger = Logger.getLogger(FavouriteRepository.class.getName());

    public Integer addFavouriteByUser (Long postId, int user_id) {

        Integer recordCount = countGetFavourite(postId, user_id);
        if(recordCount==0) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            template.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(SQL_INSERT_FAVOURITE_BY_USER_ID,
                        Statement.RETURN_GENERATED_KEYS);

                ps.setInt(1, user_id);
                ps.setLong(2, postId);

                return ps;

            }, keyHolder);

            BigInteger bigInt = (BigInteger) keyHolder.getKey();
            return bigInt.intValue();
        } else {
            return -1;
        }
    }

    public boolean deleteFavouriteByPostId(Long postId) {
        if(countFavouriteByPostId(postId)>0) {
            int isDeleted = template.update(SQL_DELETE_FAV_BY_POST_ID,postId);
            return isDeleted>0;
        }
        else {
            return true;
        }
    }

    public int countFavouriteByPostId(Long postId) {
        final SqlRowSet rs = template.queryForRowSet(SQL_COUNT_FAV_RECORDS_BY_POST_ID, postId);
        if(!rs.next()) {
            throw new HomeworkNerdException("unable to count fav records by Post Id");
        }
        return rs.getInt("count");
    }

    public boolean deleteFavouriteByUser(Integer recordId) {
        int isDeleted = template.update(SQL_DELETE_FAV_BY_RECORD_ID,recordId);
        return isDeleted>0;
    }

    public Integer countGetFavourite(Long postId, int userId) {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_FAV_BY_POST_ID,postId);
        if (!rs.first()) {
            return 0;
        }
        return rs.getInt("count");
    }

    public Optional<List<Favourite>> getAllFavouritePostsByUserId(int userId) {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_ALL_FAV_BY_USER_ID, userId);
        if (!rs.isBeforeFirst()) {
            logger.warning(">>>> FavRepository: getAllFavouritePostsByUserId: no data found");
            return Optional.empty();
        }
        else {
            List<Favourite> list = new ArrayList<>();
            while (rs.next()) {
                list.add(Favourite.create(rs));
            }
            return Optional.of(list);
        }
    }
}
