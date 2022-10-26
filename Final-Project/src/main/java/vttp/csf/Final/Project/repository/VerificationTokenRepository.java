package vttp.csf.Final.Project.repository;

import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp.csf.Final.Project.model.VerificationToken;

import static vttp.csf.Final.Project.repository.Queries.*;

@Repository
public class VerificationTokenRepository {
    
    @Autowired
    JdbcTemplate template;

    private final Logger logger = Logger.getLogger(VerificationTokenRepository.class.getName());

    public boolean saveToken(VerificationToken verificationToken) {
        int saved = template.update(SQL_INSERT_VERIFICATION_TOKEN,
         verificationToken.getUser().getUserId(),
         verificationToken.getToken());

        return saved>0;
    }

	public Optional<VerificationToken> findByToken(String token) {
        final SqlRowSet rs = template.queryForRowSet(SQL_FIND_USER_BY_VERIFICATION_TOKEN, token);
        if (!rs.first()) {
            logger.warning("VerificationTokenRepo: findByToken: no data found");
            return Optional.empty();
        }
        else return Optional.of(VerificationToken.create(rs));
	}


}
