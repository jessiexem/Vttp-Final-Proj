import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable, Output } from "@angular/core";
import { Router } from "@angular/router";
import { LocalStorageService } from "ngx-webstorage";
import { firstValueFrom, map, Subject, tap } from "rxjs";
import { LoginRequest, LoginResponse, ProfilePicResponse, SignupRequest } from "src/app/models";
import { JwtHelperService } from '@auth0/angular-jwt';

//const BASE_URL = "http://localhost:8080"
const BASE_URL = "https://askit.azurewebsites.net/"

const URL_SIGNUP = BASE_URL+"/api/auth/signup"
const URL_LOGIN = BASE_URL+"/api/auth/login"
const URL_LOGOUT = BASE_URL+"/api/auth/logout"
const URL_GENERATE_NEW_TOKEN_W_REFRESH_TOKEN = BASE_URL+"/api/auth/refresh/token"
const URL_UPDATE_PROFILE_PIC =  BASE_URL+"/api/auth/dp"

@Injectable()
export class AuthService {

    @Output()
    onLoggedIn = new Subject<boolean>()

    @Output()
    username = new Subject<string>()

    @Output()
    dpUrl = new Subject<string>()

    refreshTokenPayload = {
        refreshToken: this.getRefreshToken(),
        username: this.getUserName()
    }

    constructor(private http: HttpClient, private localStorage: LocalStorageService, private router: Router, public jwtHelper: JwtHelperService) {}

   signup(signupRequest : SignupRequest) {

    return firstValueFrom(
        this.http.post<any>(URL_SIGNUP, signupRequest, { responseType: 'text' as 'json'})
    )
   }

    login (loginRequest : LoginRequest) : Promise<boolean> {
        
        return firstValueFrom(
            this.http.post<LoginResponse>(URL_LOGIN, loginRequest)
            .pipe(
                map((data) => {
                    //store in browser's local storage
                    this.localStorage.store('authenticationToken',data.authenticationToken)
                    this.localStorage.store('username', data.username)
                    this.localStorage.store('refreshToken', data.refreshToken)
                    this.localStorage.store('expiresAt', data.expiresAt)
                    this.localStorage.store('dpUrl', data.dpUrl)

                    this.onLoggedIn.next(true)
                    this.username.next(data.username)
                    this.dpUrl.next(data.dpUrl!)
                    return true
                })
            )
        )
    }

    getJwtToken() {
        return this.localStorage.retrieve('authenticationToken')
    }

    getRefreshToken() {
        return this.localStorage.retrieve('refreshToken')
    }

    getUserName() {
        return this.localStorage.retrieve('username');
    }
    
    getExpirationTime() {
        return this.localStorage.retrieve('expiresAt');
    }

    isLoggedIn(): boolean {
        return this.getJwtToken() != null;
    }

    refreshToken() {
        const refreshTokenPayload = {
            refreshToken: this.getRefreshToken(),
            username: this.getUserName()
          }

        return this.http.post<LoginResponse>(URL_GENERATE_NEW_TOKEN_W_REFRESH_TOKEN, refreshTokenPayload)
        .pipe(tap(
            response=> {
                this.localStorage.store('authenticationToken', response.authenticationToken)
                this.localStorage.store('expiresAt', response.expiresAt)
            }
        ))
    }

    logout() {
        this.http.post(URL_LOGOUT, this.refreshTokenPayload)
        .pipe(
            map(
                data => {
                    console.log(data)
                }
            )
        )

        this.localStorage.clear('authenticationToken');
        this.localStorage.clear('username');
        this.localStorage.clear('refreshToken');
        this.localStorage.clear('expiresAt');
        this.localStorage.clear('dpUrl');
        
        // this.router.navigate(['/login'])
        
    }

      public isAuthenticated(): boolean {
        const token = this.localStorage.retrieve('authenticationToken')
        // Check whether the token is expired and return
        // true or false
        return !this.jwtHelper.isTokenExpired(token);
    }

    uploadProfilePic(file: Blob) {
        const data = new FormData()
        data.set('file', file)

        return firstValueFrom (
            this.http.post<ProfilePicResponse>(URL_UPDATE_PROFILE_PIC, data)
            .pipe(
                map((result) => {
                    console.log(result.profilePicUrl)
                    this.localStorage.store('dpUrl', result.profilePicUrl)
                    this.dpUrl.next(result.profilePicUrl!)
                })
            )
        )
    }

}