import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LocalStorageService } from 'ngx-webstorage';
import { PostService } from '../../auth/shared/post.service';
import { Post } from '../../models';

@Component({
  selector: 'app-display-posts',
  templateUrl: './display-posts.component.html',
  styleUrls: ['./display-posts.component.css']
})
export class DisplayPostsComponent implements OnInit {

  posts!: Post[]
  name: string

  constructor(private postSvc: PostService, private localStorage: LocalStorageService, private router: Router) {
    this.name = this.localStorage.retrieve("username")
    
    this.getPostsByUser(this.name)
  }

  ngOnInit(): void {
  }

  getPostsByUser(name: string) {
    this.postSvc.getAllPostsByUsername(this.name)
    .then(
      result => {
        console.info('>>>> getAllPostsByUsername result:', result)
        this.posts = result
      }
    )
    .catch(
      error => {
        console.error(">>>> getAllPostsByUsername error: ", error)
      }
    )
  }

  deletePost(pid : number) {
    if(confirm('Are you sure you want to delete this post?')) {
      this.postSvc.deletePostByPostId(pid)
      .then(
        result => {
          console.log("display-post: deletePost result:", result)
          this.getPostsByUser(this.name)
        }
      )
      .catch(
        error => {
          console.error("display-post: deletePost error:", error)
        }
      )
    }
    
  }

  newSearchPost(searchTerm : string) {
    this.router.navigate(['/'], {queryParams: {search: searchTerm}})
  }
}
