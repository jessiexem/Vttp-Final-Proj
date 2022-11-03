import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom, BehaviorSubject, tap} from "rxjs";
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

}