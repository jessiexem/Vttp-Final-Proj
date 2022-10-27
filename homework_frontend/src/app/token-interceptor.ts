import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, catchError, Observable, switchMap, throwError } from "rxjs";
import { AuthService } from "./auth/shared/auth.service";
import { LoginResponse } from "./models";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

    isTokenRefreshing : boolean = false;
    refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject(null);
    
    constructor(public authService: AuthService) { }
    
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const jwtToken = this.authService.getJwtToken()

        if (jwtToken) {
           const cloned = this.addToken(req, jwtToken)
           return next.handle(cloned)
        }

        return next.handle(req).pipe(catchError (error => {
            if (error instanceof HttpErrorResponse && error.status === 403) {
                return this.handleAuthErrors(req, next)
            } else {
                return throwError(() => new Error(error))
            }
        }))
    }

    private handleAuthErrors(req: HttpRequest<any>, next: HttpHandler) : Observable<HttpEvent<any>> {
        if (!this.isTokenRefreshing) {
            this.isTokenRefreshing = true
            this.refreshTokenSubject.next(null)

            return this.authService.refreshToken().pipe(
                switchMap((refreshTokenResponse: LoginResponse) => {
                    this.isTokenRefreshing = false;
                    this.refreshTokenSubject.next(refreshTokenResponse.authenticationToken);
                    return next.handle(this.addToken(req, refreshTokenResponse.authenticationToken));
                })
            )
        } 
        else { return throwError("handleAuthErrors - error");
        }

    }

    addToken(req: HttpRequest<any>, jwtToken: string): HttpRequest<any> {
        return req.clone({
            headers: req.headers.set("Authorization", "Bearer " + jwtToken)
        })
    }



}