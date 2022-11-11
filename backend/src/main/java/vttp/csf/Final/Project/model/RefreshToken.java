package vttp.csf.Final.Project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    private Long id;
    private String token;
    private Instant createdDate;

    public static RefreshToken create(SqlRowSet rs) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(rs.getLong("rid"));
        refreshToken.setToken(rs.getString("token"));
        refreshToken.setCreatedDate(rs.getTimestamp("created_date").toInstant());

        return refreshToken;
    }
}
