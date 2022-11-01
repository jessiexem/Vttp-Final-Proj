import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Quiz, QuizRequest } from '../models';
import { QuizService } from './quiz.service';

@Component({
  selector: 'app-quiz-welcome',
  templateUrl: './quiz-welcome.component.html',
  styleUrls: ['./quiz-welcome.component.css']
})
export class QuizWelcomeComponent implements OnInit {

  quizForm!: FormGroup;
  quizList!: Quiz[]

  constructor(private fb: FormBuilder, private quizSvc: QuizService, private router: Router) { }

  ngOnInit(): void {
    this.quizForm = this.fb.group ({
      category: this.fb.control<string>('Linux', [Validators.required]),
      difficulty: this.fb.control<string>('Easy',[Validators.required])
    })
  }

  takeQuiz() {                                                                   
    const quizReq = this.quizForm.value as QuizRequest
    this.quizSvc.takeQuiz(quizReq)
    .then( result => {
      console.info(">>>>>>takeQuiz success",result)
      this.quizList = result
      //this.quizSvc.onNewQuiz.next(result)
      this.router.navigate(['/question'])
    })
    .catch(error => {
      console.error(">>>>>>takeQuiz error", error)
    })
  }
}
