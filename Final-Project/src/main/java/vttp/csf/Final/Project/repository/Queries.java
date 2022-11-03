package vttp.csf.Final.Project.repository;

public class Queries {
    
    public static final String SQL_INSERT_USER =
        "insert into user(username, email, password, enabled) values (?, ?, ?, ?);";

    public static final String SQL_UPDATE_USER_ENABLED =
        "update user set enabled = ? where user_id=? ;";

    public static final String SQL_FIND_USER_BY_USERNAME =
            "select * from user where username=?;";

    public static final String SQL_INSERT_VERIFICATION_TOKEN =
        "insert into verification_token(user_id, token) values (?,?);";

    public static final String SQL_FIND_USER_BY_VERIFICATION_TOKEN =
        "select * from user left join verification_token on user.user_id = verification_token.user_id where verification_token.user_id=(select user_id from verification_token where token=?);";

    public static final String SQL_INSERT_REFRESH_TOKEN =
            "insert into refresh_token(token) values (?);";

    public static final String SQL_FIND_BY_REFRESH_TOKEN =
            "select * from refresh_token where token=?;";

    public static final String SQL_DELETE_BY_REFRESH_TOKEN =
            "delete from refresh_token where rid = ( select rid from (select rid from refresh_token where token= ?) as r);";

    public static final String SQL_INSERT_POST =
            "insert into post(post_name, description, tags, user_id, image_url) values (?,?,?,?,?);";

    public static final String SQL_SELECT_POST_BY_POST_ID =
            "select * from post inner join user on user.user_id = post.user_id where pid = ?";

    public static final String SQL_SELECT_ALL_POSTS =
            //"select * from post inner join user on user.user_id = post.user_id;";
        "select * from post inner join user on user.user_id = post.user_id where tags like CONCAT('%',?,'%') OR description like CONCAT('%',?,'%') OR post_name like CONCAT('%',?,'%');";

    public static final String SQL_INSERT_COMMENT =
        "insert into comment(text, vote_count, user_id, post_id) values (?,?,?,?);";
        //"insert into comment(text, user_id, post_id) values (?,?,?);";

    public static final String SQL_FIND_ALL_COMMENTS_BY_POST_ID =
            "select * from comment inner join user on user.user_id = comment.user_id \n" +
                    "inner join post on post.pid = comment.post_id where post_id=?;";

    public static final String SQL_SELECT_COMMENT_BY_CID =
            "select * from comment inner join user on user.user_id = comment.user_id \n" +
                    "inner join post on post.pid = comment.post_id where cid=?;";

    public static final String SQL_UPDATE_COMMENT_VOTE_COUNT =
            "update comment set vote_count = ? where cid = ?";

    public static final String SQL_INSERT_VOTE =
            "insert into vote(vote_type, user_id, comment_id) values (?,?,?);";

    public static final String SQL_SELECT_MOST_RECENT_VOTE_BY_USER_AND_COMMENT =
            "select * from vote inner join user on vote.user_id = user.user_id \n" +
                    "inner join comment on vote.comment_id = comment.cid\n" +
                    "inner join post on comment.post_id = post.pid\n" +
                    "where vote.user_id=? and vote.comment_id=? order by vid desc limit 1;";
}
