package vttp.csf.Final.Project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Topics {

    List<String> tags;

    public static Topics create (SqlRowSet rs) {
        Topics topics = new Topics();

        HashSet<String> set = new HashSet<>();
        while (rs.next()) {
            String tags = rs.getString("tags");
            List<String> list = new ArrayList<String>(Arrays.asList(tags.split(",")));
            for (String s : list) {
                set.add(s);
            }
        }
        List<String> tagList = new ArrayList<>(set);
        topics.setTags(tagList);
        return topics;
    }
}
