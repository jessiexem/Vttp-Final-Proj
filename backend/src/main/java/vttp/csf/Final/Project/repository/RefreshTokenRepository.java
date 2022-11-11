package vttp.csf.Final.Project.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import vttp.csf.Final.Project.model.RefreshToken;

import java.util.Optional;
import java.util.logging.Logger;

import static vttp.csf.Final.Project.repository.Queries.*;

@Repository
public class RefreshTokenRepository {

    @Autowired
    JdbcTemplate template;

    private final Logger logger = Logger.getLogger(RefreshTokenRepository.class.getName());

    public Optional<RefreshToken> saveToken(RefreshToken refreshToken) {
        int saved = template.update(SQL_INSERT_REFRESH_TOKEN,
                refreshToken.getToken());

        if (saved>0) {
            return findByToken(refreshToken.getToken());
        } else return Optional.empty();
    }

    public Optional<RefreshToken> findByToken(String token) {
        final SqlRowSet rs = template.queryForRowSet(SQL_FIND_BY_REFRESH_TOKEN, token);
        if (!rs.first()) {
            logger.warning("RefreshTokenRepo: findByToken: no data found");
            return Optional.empty();
        }
        else return Optional.of(RefreshToken.create(rs));
    }

    public boolean deleteToken(String token) {
        int deleted = template.update(SQL_DELETE_BY_REFRESH_TOKEN, token);
        return deleted>0;
    }
}
