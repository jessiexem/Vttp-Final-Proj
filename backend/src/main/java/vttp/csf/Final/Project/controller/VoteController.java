package vttp.csf.Final.Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vttp.csf.Final.Project.dto.VoteDto;
import vttp.csf.Final.Project.service.VoteService;

@RestController
@RequestMapping("/api/vote")
public class VoteController {

    @Autowired
    private VoteService voteSvc;

    @PostMapping
    public ResponseEntity<String> vote (@RequestBody VoteDto voteDto) {
        voteSvc.saveVote(voteDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Vote has been saved for comment:"+voteDto.getCommentId());
    }
}
