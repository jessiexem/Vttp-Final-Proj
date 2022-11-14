import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { VotePayload } from "src/app/models";

//const BASE_URL = "http://localhost:8080"
const BASE_URL = "https://askit.azurewebsites.net"

const URL_VOTE_COMMENT = BASE_URL+"/api/vote" 

@Injectable()
export class VoteService {

    constructor(private http: HttpClient) {}

    voteComment(VotePayload : VotePayload) {
        return firstValueFrom(
            this.http.post(URL_VOTE_COMMENT, VotePayload, { responseType: 'text' as 'json'})
        )
    }
}
