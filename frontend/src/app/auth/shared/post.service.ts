import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom} from "rxjs";
import { CreatePostPayload, Favourite, Post, Topics } from "src/app/models";

//const BASE_URL = "http://localhost:8080"
const BASE_URL = "https://askit.azurewebsites.net"

const URL_GET_ALL_POSTS = BASE_URL+"/api/posts"
const URL_GET_POST_BY_ID = BASE_URL+"/api/posts/"
const URL_GET_ALL_POSTS_BY_USERNAME = BASE_URL+"/api/posts/profile/"
const URL_DELETE_POST_BY_USER_BY_POST_ID = BASE_URL+"/api/posts/delete/"
const URL_GET_ALL_TOPICS = BASE_URL+"/api/posts/topics/"
const URL_ADD_TO_FAVOURITE = BASE_URL+"/api/fav"
const URL_DELETE_FAVOURITE_BY_RECORD_ID = BASE_URL+"/api/fav/delete/"

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

    getAllTopics() {
        return firstValueFrom(
            this.http.get<Topics>(URL_GET_ALL_TOPICS)
        )
    }

}