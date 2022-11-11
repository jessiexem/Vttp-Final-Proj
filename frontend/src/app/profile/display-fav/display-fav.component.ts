import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PostService } from 'src/app/auth/shared/post.service';
import { Favourite } from 'src/app/models';

@Component({
  selector: 'app-display-fav',
  templateUrl: './display-fav.component.html',
  styleUrls: ['./display-fav.component.css']
})
export class DisplayFavComponent implements OnInit {

  favourites!: Favourite[]


  constructor(private postSvc: PostService, private router: Router) { 
    this.getAllFavouriteByUser()
  }

  ngOnInit(): void {
  }

  getAllFavouriteByUser() {
    this.postSvc.getAllFavourite()
    .then(
      result => {
        console.info('>>>> getAllFavourite result:', result)
        this.favourites = result
      }
    )
    .catch(
      error => {
        console.error(">>>> getAllFavourite error: ", error)
      }
    )
  }

  newSearchPost(searchTerm : string) {
    this.router.navigate(['/'], {queryParams: {search: searchTerm}})
  }

  deleteFav(recordId : number) {
    if(confirm('Are you sure you want to remove this from your Favourites?')) {
      this.postSvc.deleteFavouriteByRecordId(recordId)
      .then(
        result => {
          console.log("display-fav: deleteFav result:", result)
          this.getAllFavouriteByUser()
        }
      )
      .catch(
        error => {
          console.error("display-post: deletePost error:", error)
        }
      )
    }
  }

}
