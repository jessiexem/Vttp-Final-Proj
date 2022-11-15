package vttp.csf.Final.Project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vttp.csf.Final.Project.dto.FavouriteResponse;
import vttp.csf.Final.Project.dto.PostResponse;
import vttp.csf.Final.Project.exception.HomeworkNerdException;
import vttp.csf.Final.Project.mapper.PostMapper;
import vttp.csf.Final.Project.model.Favourite;
import vttp.csf.Final.Project.model.NotificationEmail;
import vttp.csf.Final.Project.model.Post;
import vttp.csf.Final.Project.model.User;
import vttp.csf.Final.Project.repository.FavouriteRepository;
import vttp.csf.Final.Project.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavouriteService {

    @Autowired
    private FavouriteRepository favouriteRepo;

    @Autowired
    private AuthService authSvc;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private MailContentBuilder mailContentBuilder;

    @Autowired
    private MailService mailSvc;

    public Integer addToFavourite(Long postId) {
        User user = authSvc.getCurrentUser();
        Integer recordId = favouriteRepo.addFavouriteByUser(postId, user.getUserId());

        Optional<Post> optPost = postRepo.getPostById(postId);
        if(optPost.isEmpty()) {
            throw new HomeworkNerdException("Favourite Service: No such post with post id: "+ postId);
        } else {
            Post post = optPost.get();
            String message = mailContentBuilder.build(user.getUsername() + " favourited your post: \n\""+ post.getPostName() + "\"");
            sendCommentNotification(message, user);
            return recordId;
        }
    }

    public List<FavouriteResponse> getAllFavouritePostsByUser() {
        User user = authSvc.getCurrentUser();

        Optional<List<Favourite>> optList = favouriteRepo.getAllFavouritePostsByUserId(user.getUserId());

        if (optList.isEmpty()) {
            return null;
        }
        else {
            List<Favourite> favList = optList.get();
            List<FavouriteResponse> favResponseList = new ArrayList<>();
            favList.stream()
                    .forEach(favourite -> {
                        FavouriteResponse favResp = new FavouriteResponse();
                        favResp.setUserId(favourite.getUserId());
                        favResp.setRecordId(favourite.getRecordId());
                        favResp.setPostResponse(postMapper.mapPostToDto(favourite.getPost()));
                        favResponseList.add(favResp);
                    });
            return favResponseList;
        }
    }

    public boolean deleteFavByUser(Integer recordId) {
        boolean isDeleted = favouriteRepo.deleteFavouriteByUser(recordId);
        return isDeleted;
    }

    private void sendCommentNotification(String message, User user) {
        mailSvc.sendMail(new NotificationEmail("SocialIT: "+user.getUsername()+" favourited your post!",
                user.getEmail(), message));
    }
}
