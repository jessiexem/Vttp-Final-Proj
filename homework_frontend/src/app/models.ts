export interface SignupRequest {
    email: string;
    username: string;
    password: string;
}

export interface LoginRequest {
    username: string;
    password: string;
}

export interface LoginResponse {
    authenticationToken: string;
    refreshToken: string;
    expiresAt: Date;
    username: string;
}

export interface Post {
    id: number;
    postName: string;
    description: string;
    userName: string;
    commentCount: number;
    tags: string[];
}

export interface CreatePosttPayload{
    postName: string;
    description: string;
    tags: string[];
}

export interface Comment {
    id: number;
    postId: number;
    createdDate : Date;
    text: string;
    username: string;
    voteCount: number
}

export enum VoteType {
    UPVOTE = 'UPVOTE',
    DOWNVOTE = 'DOWNVOTE'
}

export interface VotePayload {
    voteType: VoteType;
    commentId: number;
}

export interface CommentPayload{
    text: string;
    postId: number;
    username: string;
}