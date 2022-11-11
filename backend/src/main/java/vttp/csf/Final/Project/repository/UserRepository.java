package vttp.csf.Final.Project.repository;

import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;

import vttp.csf.Final.Project.model.User;
import static vttp.csf.Final.Project.repository.Queries.*;

@Repository
public class UserRepository {
    
    @Autowired
    JdbcTemplate template;

    private final Logger logger = Logger.getLogger(UserRepository.class.getName());

    public Integer saveUser(User user) {

        logger.info("in UserRepo: saveUser: "+ user.getUsername());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(SQL_INSERT_USER,
                Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setBoolean(4, user.isEnabled());
            return ps;

        }, keyHolder);

        BigInteger bigInt = (BigInteger) keyHolder.getKey();
        return bigInt.intValue();
    }

	public boolean updateUser(User user) {
        int updated = template.update(SQL_UPDATE_USER_ENABLED, user.isEnabled(), user.getUserId());
        return updated == 1;
	}

    public Optional<User> findUserByUsername(String username) {
        final SqlRowSet rs = template.queryForRowSet(SQL_FIND_USER_BY_USERNAME, username);
        if(!rs.first()) {
            logger.warning(">>>> UserRepository: findUserByUsername: no data found");
            return Optional.empty();
        }
        else return Optional.of(User.create(rs));
    }

    public boolean updateProfilePic(String fileHash, int userId) {
        int updated = template.update(SQL_UPDATE_USER_PROFILE_PIC, fileHash, userId);
        return updated == 1;
    }

    public String getExistingDpImageUrl(int userId) {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_DP_IMAGE_URL_BY_USER, userId);
        if(!rs.first()) {
            return null;
        } else {
            return rs.getString("dp_image_url");
        }
    }
}
