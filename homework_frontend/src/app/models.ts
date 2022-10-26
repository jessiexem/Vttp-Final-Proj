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
}