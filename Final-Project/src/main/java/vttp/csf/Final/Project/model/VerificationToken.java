package vttp.csf.Final.Project.model;

import java.time.Instant;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken {
    
    private int id;
    private String token;
    private User user; //only need userId in SQL
    private Instant expiryDate;

    public static VerificationToken create(SqlRowSet rs) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setId(rs.getInt("token_id"));
        verificationToken.setToken(rs.getString("token"));
        User user = User.create(rs);
        verificationToken.setUser(user);

        return verificationToken;
    }
}
