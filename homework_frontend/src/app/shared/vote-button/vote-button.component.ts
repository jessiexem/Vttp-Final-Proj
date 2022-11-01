// import { Component, Input, OnInit, Output, ViewChild } from '@angular/core';
// import { AuthService } from 'src/app/auth/shared/auth.service';
// import { ToastrService } from 'ngx-toastr';
// import { VoteService } from './vote.service';
// import { VotePayload, VoteType } from 'src/app/models';
// import { Comment } from "src/app/models";
// import { faArrowUp, faArrowDown } from '@fortawesome/free-solid-svg-icons';
// import { CommentService } from 'src/app/comment/comment.service';
// import { Subject } from 'rxjs';
// import { ViewPostComponent } from 'src/app/post/view-post.component';


// @Component({
//   selector: 'app-vote-button',
//   templateUrl: './vote-button.component.html',
//   styleUrls: ['./vote-button.component.css']
// })
// export class VoteButtonComponent implements OnInit {

  // @Input()
  // comment!: Comment

  // isLoggedIn!: boolean;
  // votePayload!: VotePayload;
  // faArrowUp = faArrowUp;
  // faArrowDown = faArrowDown;
  // upvoteColor!: string;
  // downvoteColor!: string;

  // @ViewChild(ViewPostComponent)
  // viewPost!: ViewPostComponent;

  // constructor(private authSvc: AuthService, private commentSvc: CommentService,
  //   private voteSvc: VoteService, private toastr: ToastrService) 
  //   {
  //     // this.votePayload = {
  //     //   voteType: undefined,
  //     //   postId: undefined
  //     // }
  //     this.authSvc.onLoggedIn.subscribe((data: boolean) => this.isLoggedIn = data);
  //   }

  // ngOnInit(): void {
  // }

  // upvoteComment() {
  //   this.votePayload.voteType = VoteType.UPVOTE;
  //   this.vote();
  //   this.downvoteColor = '';
  // }

  // downvoteComment() {
  //   this.votePayload.voteType = VoteType.DOWNVOTE;
  //   this.vote();
  //   this.upvoteColor = '';
  // }

  // private vote() {
  //   this.votePayload.commnentId = this.comment.id
  //   this.voteSvc.voteComment(this.votePayload)
  //   this.updateCommentDetails()
  // }

  // private updateCommentDetails() {
  //   this.viewPost.ngOnInit()
  // }
// }
