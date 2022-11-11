import { Component, OnInit } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { CommentService } from 'src/app/comment/comment.service';
import { CommentSummary } from 'src/app/models';

@Component({
  selector: 'app-display-comments',
  templateUrl: './display-comments.component.html',
  styleUrls: ['./display-comments.component.css']
})
export class DisplayCommentsComponent implements OnInit {

  commentSummaryList!: CommentSummary[]
  name: string

  constructor(private commentSvc: CommentService, private localStorage: LocalStorageService) {
    this.name = this.localStorage.retrieve("username")
    this.getCommentsByUser(this.name)
  }

  ngOnInit(): void {
  }

  getCommentsByUser(name: string) {
    this.commentSvc.getAllCommentsByUsername(this.name)
    .then(
      result => {
        console.info('>>>> getCommentsByUser result:', result)
        this.commentSummaryList = result
      }
    )
    .catch(
      error => {
        console.error(">>>> getCommentsByUser error: ", error)
      }
    )
  }

  deleteComment(cid : number) {
    if(confirm('Are you sure you want to delete this comment?')) {
      this.commentSvc.deleteCommentByCommentId(cid)
      .then(
        result => {
          console.log("display-comment: deleteComment result:", result)
          this.getCommentsByUser(this.name)
        }
      )
      .catch(
        error => {
          console.error("display-comment: deleteComment error:", error)
        }
      )
    }
  }
}
