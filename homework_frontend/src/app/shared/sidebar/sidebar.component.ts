import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PostService } from 'src/app/auth/shared/post.service';
import { Topics } from 'src/app/models';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

  topics!: Topics

  constructor(private router: Router, private postSvc: PostService) { }

  ngOnInit(): void {  
    this.postSvc.getAllTopics()
    .then(
      result => {
        console.log("getAllTopics result:",result)
        this.topics = result
      }
    )
    .catch(
      error => {
        console.error("getAllTopics error:",error)
      }
    )
  }

  newSearchPost(searchTerm : string) {
    this.router.navigate(['/'], {queryParams: {search: searchTerm}})
  }

  // goToCreatePost() {
  //   this.router.navigateByUrl('/create-post')
  // }

  // goToQuizHome() {
  //   this.router.navigateByUrl('/quizHome')
  // }
}
