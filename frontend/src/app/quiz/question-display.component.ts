import { Component, OnInit } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { interval, Subscription } from 'rxjs';
import { Quiz } from '../models';
import { QuizService } from './quiz.service';

@Component({
  selector: 'app-question-display',
  templateUrl: './question-display.component.html',
  styleUrls: ['./question-display.component.css']
})
export class QuestionDisplayComponent implements OnInit {

  sub$!: Subscription
  quizList!: Quiz[]
  interval$: any;

  name: string = "";
  currentQuestion: number = 0;
  points: number = 0;
  counter = 60;
  correctAnswer: number = 0;
  inCorrectAnswer: number = 0;
  progress: number = 0;
  //isQuizCompleted : boolean = false;
  
  constructor(private quizSvc: QuizService, private localStorage: LocalStorageService) { }

  ngOnInit(): void {
    this.name = this.localStorage.retrieve("username")
    console.log("name:",this.name)
    this.getAllQuestions()
    this.startCounter();
  }

  getAllQuestions() {
    this.sub$ = this.quizSvc.onNewQuiz.subscribe(
      result => {
        console.log("quizList:",result)
        this.quizList = result
      }
    )
  }

  startCounter() {
    this.interval$ = interval(1000)
      .subscribe(val => {
        this.counter--;
        if (this.counter === 0) {
          this.nextQuestion();
          this.points -= 10;
          this.getProgressPercent()
        }
        if (this.currentQuestion > 9) {
          this.interval$.unsubscribe();
        }
      });
    setTimeout(() => {
      this.interval$.unsubscribe();
    }, 600000);
  }

  stopCounter() {
    this.interval$.unsubscribe();
    this.counter = 0;
  }

  resetCounter() {
    this.stopCounter();
    this.counter = 60;
    this.startCounter();
  }

  // previousQuestion() {
  //   this.currentQuestion--;
  // }

  nextQuestion() {
    //alert(this.currentQuestion);
    this.currentQuestion++
    this.points -= 10
    this.resetCounter()
  }

  answer(currentQno: number, optionIdx: number) {

    console.log(">>answer() current Qno:", currentQno, "optionIdx:", optionIdx)

    if(currentQno === this.quizList.length){
      //this.isQuizCompleted = true;
      this.stopCounter();
    }

    console.log("is it correct?",this.quizList[currentQno-1].answerArray[optionIdx])

    if (this.quizList[currentQno-1].answerArray[optionIdx] === "true") {
      this.points += 10;
      this.correctAnswer++;
      setTimeout(() => {
        this.currentQuestion++;
        this.resetCounter();
        this.getProgressPercent();
      }, 1000);


    } else {
      setTimeout(() => {
        this.currentQuestion++;
        this.inCorrectAnswer++;
        this.resetCounter();
        this.getProgressPercent();
      }, 1000);

      this.points -= 10;
    }
  }

  getProgressPercent() {
    console.info("quizlist length:" ,this.quizList.length)

    this.progress = ((this.currentQuestion / this.quizList.length) * 100);
    return this.progress;
  }

  // resetQuiz() {
  //   this.resetCounter();
  //   this.getAllQuestions();
  //   this.points = 0;
  //   this.counter = 60;
  //   this.currentQuestion = 0;
  //   this.progress = 0;

  // }

  ngOnDestroy(): void {
    this.sub$.unsubscribe()
  }

}
