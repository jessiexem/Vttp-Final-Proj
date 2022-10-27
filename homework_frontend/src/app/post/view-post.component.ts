import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from '../auth/shared/post.service';
import { CommentService } from '../comment/comment.service';
import { CommentPayload, Post, VotePayload, VoteType } from '../models';
import { Comment } from "src/app/models";
import { faArrowDown, faArrowUp } from '@fortawesome/free-solid-svg-icons';
import { VoteService } from '../shared/vote-button/vote.service';
import { ToastrService } from 'ngx-toastr';
import { throwError } from 'rxjs';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-view-post',
  templateUrl: './view-post.component.html',
  styleUrls: ['./view-post.component.css']
})

export class ViewPostComponent implements OnInit {

  post!: Post
  postId!: number;
  comments!: Comment[]
  commentForm!: FormGroup

  // isLoggedIn!: boolean;
  votePayload!: VotePayload;
  faArrowUp = faArrowUp;
  faArrowDown = faArrowDown;
  upvoteColor!: string;
  downvoteColor!: string;
  commentPayload!: CommentPayload;

  constructor(private postService: PostService, private activateRoute: ActivatedRoute, private commentSvc: CommentService,
    private voteSvc: VoteService, private toastr: ToastrService, private router: Router, private fb: FormBuilder) { 
    this.postId = this.activateRoute.snapshot.params['id'];
    
    this.votePayload = <VotePayload>{}
    this.commentPayload = <CommentPayload>{
      text: '',
      postId: this.postId
    }
    //this.authSvc.onLoggedIn.subscribe((data: boolean) => this.isLoggedIn = data);
  }

  ngOnInit(): void {
    this.getPostById()
    this.getCommentsByPid()
    this.commentForm = this.createCommentForm()
  }

  private getPostById() {
    this.postService.getPostById(this.postId)
    .then(
      result => {
        console.info('getPostById result:', result)
        this.post = result
      }
    )
    .catch(
      error => {
        console.error(">>>> View Post: performGetPostById error: ", error)
        //alert(`>>> performGetPostById error: ${JSON.stringify(error)}`)
      }
    )
  }

  private getCommentsByPid() {
    this.commentSvc.getAllCommentsByPost(this.postId)
    .then(
      result => {
        console.info("View Post: getCommentsByPid result:", result)
        this.comments = result
      }
    )
    .catch(
      error => {
        console.error(">>>> View Post: getCommentsByPid error", error)
        //alert(`>>> View Post: getCommentsByPid error: ${JSON.stringify(error)}`)
      }
    )
  }

  upvoteComment(cid: number) {
    this.votePayload.voteType = VoteType.UPVOTE;
    this.vote(cid);
    this.downvoteColor = '';
  }

  downvoteComment(cid: number) {
    this.votePayload.voteType = VoteType.DOWNVOTE;
    this.vote(cid);
    this.upvoteColor = '';
  }

  private vote(cid: number) {
    this.votePayload.commentId = cid
    this.voteSvc.voteComment(this.votePayload)
    .catch(error => {
      if (error.error.message.length > 0) {
        this.toastr.error(error.error.message)
      }
    })

    this.router.navigateByUrl('/', {skipLocationChange: true}).then(
      () => {
        this.router.navigate(['/view-post/'+this.postId])
      }
    )

  }

  createCommentForm(): FormGroup {
    return this.fb.group ({
      text: this.fb.control<string>('')
    })
  }

  createComment() {
    this.commentPayload.text = this.commentForm.value['text']

    //call service
    this.commentSvc.createComment(this.commentPayload)
    .then(
      result => {
        console.log("View Post: createComment result:", result)
      }
    )
    .catch(
      error => {
        console.error(">>>> View Post: createComment error", error)
      }
    )
    this.commentForm.reset()
    this.getCommentsByPid()
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(
      () => {
        this.router.navigate(['/view-post/'+this.postId])
      }
    )
  }
}
