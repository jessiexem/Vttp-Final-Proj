package vttp.csf.Final.Project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsSummaryDto {

    private Long id;
    private Long postId;
    private String postName;
    private Date createdDate;
    private String text;
    private String username;
    private Integer voteCount;
}
