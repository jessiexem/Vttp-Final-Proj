import { P } from "@angular/cdk/keycodes";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom, BehaviorSubject, tap } from "rxjs";
import { CreatePostPayload, Favourite, Post } from "src/app/models";

const URL_GET_ALL_POSTS = "http://localhost:8080/api/posts"
const URL_GET_POST_BY_ID = "http://localhost:8080/api/posts/"
const URL_GET_ALL_POSTS_BY_USERNAME = "http://localhost:8080/api/posts/profile/"
const URL_DELETE_POST_BY_USER_BY_POST_ID = "http://localhost:8080/api/posts/delete/"
const URL_ADD_TO_FAVOURITE = "http://localhost:8080/api/fav"
const URL_DELETE_FAVOURITE_BY_RECORD_ID = "http://localhost:8080/api/fav/delete/"

@Injectable()
export class PostService {

    constructor(private http: HttpClient) {}

    getAllPostsByUsername(username: string) {
        return firstValueFrom(
            this.http.get<Post[]>(URL_GET_ALL_POSTS_BY_USERNAME+username)
        )
    }

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

    deletePostByPostId(pid : number) {

        return firstValueFrom(
            this.http.delete(URL_DELETE_POST_BY_USER_BY_POST_ID+pid, { responseType: 'text' as 'json'})
        )
    }

    addToFavourite(postId : number) {
        return firstValueFrom(
            this.http.post(URL_ADD_TO_FAVOURITE, {postId : postId}, { responseType: 'text' as 'json'})
        )
    }

    getAllFavourite() {
        return firstValueFrom(
            this.http.get<Favourite[]>(URL_ADD_TO_FAVOURITE)
        )
    }

    deleteFavouriteByRecordId(recordId : number) {

        return firstValueFrom(
            this.http.delete(URL_DELETE_FAVOURITE_BY_RECORD_ID+recordId, { responseType: 'text' as 'json'})
        )
    }

}