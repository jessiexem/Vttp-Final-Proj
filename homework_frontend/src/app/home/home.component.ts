import { Component, OnInit } from '@angular/core';
import { PostService } from '../auth/shared/post.service';
import { Post } from '../models';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  posts!: Post[]

  constructor(private postSvc: PostService) { 
    this.postSvc.getAllPosts()
    .then (
      result => {
        console.info('>>>> getAllPosts result:', result)
        this.posts = result
      }
    )
    .catch(
      error => {
        console.error(">>>> performGetPosts error: ", error)
        alert(`>>> performGetPosts error: ${JSON.stringify(error)}`)
      }
    )
  }

  ngOnInit(): void {
  }

}
