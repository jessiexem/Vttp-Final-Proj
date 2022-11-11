package vttp.csf.Final.Project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsDto {

    private Long id;
    private Long postId;
    private Date createdDate;
    private String text;
    private String username;
    private String commenterDpImageUrl;
    private Integer voteCount;
}
