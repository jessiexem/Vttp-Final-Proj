import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom, BehaviorSubject, tap } from "rxjs";
import { CreatePostPayload, Post } from "src/app/models";

const URL_GET_ALL_POSTS = "http://localhost:8080/api/posts"
const URL_GET_POST_BY_ID = "http://localhost:8080/api/posts/"

@Injectable()
export class PostService {

    constructor(private http: HttpClient) {}

    // getAllPosts() {
    //     return firstValueFrom(
    //         this.http.get<Post[]>(URL_GET_ALL_POSTS)
    //     )
    // }

    searchPosts(searchTerm : string) {
        const params = new HttpParams().set("searchTerm", searchTerm)

        return firstValueFrom (
            this.http.get<Post[]>(URL_GET_ALL_POSTS, { params })
        )
    }

    getPostById(pid : number) {
        return firstValueFrom(
            this.http.get<Post>(URL_GET_POST_BY_ID+pid)
        )
    }

    createPost(createPostPayload : CreatePostPayload, file: Blob) {
        const data = new FormData()
        data.set('postName', createPostPayload.postName)
        data.set('description', createPostPayload.description)
        data.set('tags', createPostPayload.tags)
        data.set('file', file)

        return firstValueFrom (
            this.http.post(URL_GET_ALL_POSTS, data, { responseType: 'text' as 'json'})
        )
    }

    // createPost(createPostPayload : CreatePostPayload) {
    //     return firstValueFrom (
    //         this.http.post(URL_GET_ALL_POSTS, createPostPayload, { responseType: 'text' as 'json'})
    //     )
    // }

}