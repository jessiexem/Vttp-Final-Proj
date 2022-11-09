package vttp.csf.Final.Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vttp.csf.Final.Project.dto.FavouriteRequest;
import vttp.csf.Final.Project.dto.FavouriteResponse;
import vttp.csf.Final.Project.service.FavouriteService;

import java.util.List;

@RestController
@RequestMapping(path="/api/fav")
public class FavouriteController {

    @Autowired
    private FavouriteService favouriteSvc;

    @PostMapping
    public ResponseEntity<String> addFavourite(@RequestBody FavouriteRequest favouriteRequest) {
        Integer recordId = favouriteSvc.addToFavourite(favouriteRequest.getPostId());
        if (recordId == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Post %d has already already exists in Favourites", favouriteRequest.getPostId()));
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Favourite record %d has been saved successfully.", recordId));
        }
    }

    @GetMapping
    public ResponseEntity<List<FavouriteResponse>> getAllFavouritesByUser() {
        List<FavouriteResponse> list = favouriteSvc.getAllFavouritePostsByUser();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFavouriteByUser(@PathVariable (name="id") Integer recordId) {
        boolean isDeleted = favouriteSvc.deleteFavByUser(recordId);
        if(!isDeleted) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to delete favourite");
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted favourite with id:"+recordId);
        }
    }


}
