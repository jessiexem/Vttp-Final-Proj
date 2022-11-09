import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { Comment, CommentPayload, CommentSummary } from "src/app/models";

const URL_GET_ALL_COMMENTS_BY_PID = "http://localhost:8080/api/comments"
const URL_POST_COMMENT = "http://localhost:8080/api/comments"
const URL_GET_ALL_COMMENTS_BY_USERNAME = "http://localhost:8080/api/comments/profile/"
const URL_DELETE_COMMENT_BY_USER_BY_CID = "http://localhost:8080/api/comments/delete/"

@Injectable()
export class CommentService {

    constructor(private http: HttpClient) {}
    
    getAllCommentsByPost(pid : number) {

        const params = new HttpParams().set('pid',pid)

        return firstValueFrom(
            this.http.get<Comment[]>(URL_GET_ALL_COMMENTS_BY_PID, {params})
        )
    }

    createComment(commentPayload : CommentPayload) {
        return firstValueFrom(
            this.http.post<any>(URL_POST_COMMENT, commentPayload, { responseType: 'text' as 'json'})
        )
    }

    getAllCommentsByUsername(username: string) {
        return firstValueFrom(
            this.http.get<CommentSummary[]>(URL_GET_ALL_COMMENTS_BY_USERNAME+username)
        )
    }

    deleteCommentByCommentId(cid: number) {
        return firstValueFrom(
            this.http.delete(URL_DELETE_COMMENT_BY_USER_BY_CID+cid, { responseType: 'text' as 'json'})
        )
    }


}