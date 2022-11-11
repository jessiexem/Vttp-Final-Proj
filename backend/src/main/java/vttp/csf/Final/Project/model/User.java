package vttp.csf.Final.Project.model;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int userId;
    private String username;
    private String email;
    private String password;
    private boolean enabled;
    private Date created;
    private String dpImageUrl;

    public static User create (SqlRowSet rs) {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setEnabled(rs.getBoolean("enabled"));
        user.setCreated(rs.getDate("created_date"));
        if(rs.getString("dp_image_url")!=null) {
            user.setDpImageUrl(rs.getString("dp_image_url"));
        }
        return user;
    }
}

