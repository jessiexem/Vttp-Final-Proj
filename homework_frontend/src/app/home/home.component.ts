import { E } from '@angular/cdk/keycodes';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { PostService } from '../auth/shared/post.service';
import { Post } from '../models';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  posts!: Post[]
  searchValue = ''
  sub$!: Subscription

  ngOnInit(): void {
  }

  constructor(private postSvc: PostService, private router: Router, private ar : ActivatedRoute) { 
    ar.queryParams.subscribe(
      v => {
        console.info(v['search'])
        if (!v['search']) {
          this.newSearchPost('')
        } else {
          this.newSearchPost(v['search'])
         }
       }
    )
  }

  goToPost(id: number): void {
    this.router.navigateByUrl('/view-post/' + id);
  }

  newSearchPost(searchTerm : string) {
    this.searchValue = searchTerm
    this.postSvc.searchPosts(searchTerm)
    .then(
      result => {
        console.info("-----search posts result:", result)
        this.posts = result
      }
    ).catch(
      error => {
        console.error(">>>> performSearchPosts error: ", error)
      }
    )

    this.router.navigate(['/'], {queryParams: {search: this.searchValue}})
  }

  ngOnDestroy(): void {
    if (this.sub$) {
      this.sub$.unsubscribe()
    }
  }
}
