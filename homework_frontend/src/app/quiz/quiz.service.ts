import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom, BehaviorSubject, tap, catchError} from "rxjs";
import { Quiz, QuizRequest } from "../models";

const URL_BASE = "http://localhost:8080/api/quiz/"

@Injectable()
export class QuizService {

    onNewQuiz = new BehaviorSubject<Quiz[]>([])
    
    constructor (private http: HttpClient) {}

    takeQuiz(quizRequest : QuizRequest) {
        return firstValueFrom(
            this.http.post<Quiz[]>(URL_BASE,quizRequest).pipe(
                tap (data => {
                    console.info("in tap data:",data)
                    this.onNewQuiz.next(data)
                })
            )
        )
    }

    // private handleError(operation: String) {
    //     return (err: any) => {
    //         let errMsg = `error in ${operation}() retrieving ${URL_BASE}`;
    //         console.log(`${errMsg}:`, err)
    //         if(err instanceof HttpErrorResponse) {
    //             // you could extract more info about the error if you want, e.g.:
    //             console.log(`status: ${err.status}, ${err.statusText}`);
    //             // errMsg = ...
    //         }
    //         return Observable.throw(errMsg);
    //     }
    // }

}