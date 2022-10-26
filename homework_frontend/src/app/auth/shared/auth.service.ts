import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { LocalStorageService } from "ngx-webstorage";
import { firstValueFrom, map, tap } from "rxjs";
import { LoginRequest, LoginResponse, SignupRequest } from "src/app/models";

const URL_SIGNUP = "http://localhost:8080/api/auth/signup"
const URL_LOGIN = "http://localhost:8080/api/auth/login"
const URL_GENERATE_NEW_TOKEN_W_REFRESH_TOKEN = "http://localhost:8080/api/auth/refresh/token"

@Injectable()
export class AuthService {


    constructor(private http: HttpClient, private localStorage: LocalStorageService) {}

   signup(signupRequest : SignupRequest) {

    // const headers = new HttpHeaders()
    // .set('Content-Type', 'application/json')
    // .set('Accept','application/json')

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

}