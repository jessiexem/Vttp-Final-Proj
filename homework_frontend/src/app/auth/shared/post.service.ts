import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { Post } from "src/app/models";

const URL_GET_ALL_POSTS = "http://localhost:8080/api/posts/"

@Injectable()
export class PostService {

    constructor(private http: HttpClient) {}

    getAllPosts() {
        return firstValueFrom(
            this.http.get<Post[]>(URL_GET_ALL_POSTS)
        )
    }
}