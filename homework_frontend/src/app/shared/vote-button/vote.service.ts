import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { VotePayload } from "src/app/models";

const URL_VOTE_COMMENT = "http://localhost:8080/api/vote" 

@Injectable()
export class VoteService {

    constructor(private http: HttpClient) {}

    voteComment(VotePayload : VotePayload) {
        return firstValueFrom( 
            this.http.post(URL_VOTE_COMMENT, VotePayload)
        )
    }
}
