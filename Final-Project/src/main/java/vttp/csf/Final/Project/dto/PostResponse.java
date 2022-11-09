package vttp.csf.Final.Project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private Long id;
    private String postName;
    private String description;
    private String userName;
    private Integer commentCount;
//    private String tags;
    private String imageUrl;
    private List<String> tags;
    private Instant createdDate;
}
