package vttp.csf.Final.Project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Topic {

    private Long id;
    private String topic_name;
    private String description;
    private List<Post> posts;
    private User user;
}
