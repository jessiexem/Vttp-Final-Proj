import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom, BehaviorSubject, tap} from "rxjs";
import { Quiz, QuizRequest } from "../models";

//const BASE_URL = "http://localhost:8080"
const BASE_URL = "https://askit.azurewebsites.net"

const URL_GET_QUIZ = BASE_URL+"/api/quiz/"

@Injectable()
export class QuizService {

    onNewQuiz = new BehaviorSubject<Quiz[]>([])
    
    constructor (private http: HttpClient) {}

    takeQuiz(quizRequest : QuizRequest) {
        return firstValueFrom(
            this.http.post<Quiz[]>(URL_GET_QUIZ,quizRequest).pipe(
                tap (data => {
                    console.info("in tap data:",data)
                    this.onNewQuiz.next(data)
                })
            )
        )
    }

}