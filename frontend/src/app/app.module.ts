import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material.module';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http'
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { SignupComponent } from './auth/signup/signup.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from './auth/shared/auth.service';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { ToastrModule } from 'ngx-toastr';
import { TokenInterceptor } from './token-interceptor';
import { HomeComponent } from './home/home.component';
import { PostService } from './auth/shared/post.service';
import { SidebarComponent } from './shared/sidebar/sidebar.component';
import { ViewPostComponent } from './post/view-post.component';
import { CommentService } from './comment/comment.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { VoteService } from './shared/vote-button/vote.service';
import { EditorModule } from '@tinymce/tinymce-angular';
import { CreatePostComponent } from './post/create-post/create-post.component';
import { QuizWelcomeComponent } from './quiz/quiz-welcome.component';
import { QuizService } from './quiz/quiz.service';
import { QuestionDisplayComponent } from './quiz/question-display.component';
import { ChangeBgDirective } from './change-bg.directive';
import { SearchPostComponent } from './post/search-post/search-post.component';
import { AuthGuardService as AuthGuard} from './auth/shared/authguard.service';
import { JwtHelperService, JWT_OPTIONS  } from '@auth0/angular-jwt';
import { ProfileComponent } from './profile/profile.component';
import { DisplayPostsComponent } from './profile/display-posts/display-posts.component';
import { DisplayCommentsComponent } from './profile/display-comments/display-comments.component';
import { DisplayFavComponent } from './profile/display-fav/display-fav.component';

const appRoutes: Routes = [
  { path: '', component: HomeComponent, canActivate: [AuthGuard]},
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'view-post/:id', component: ViewPostComponent, canActivate: [AuthGuard] },
  { path: 'create-post', component: CreatePostComponent, canActivate: [AuthGuard] },
  { path: 'quizHome', component: QuizWelcomeComponent, canActivate: [AuthGuard] },
  { path: 'question', component: QuestionDisplayComponent, canActivate: [AuthGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '/',pathMatch: 'full'}
]

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignupComponent,  
    HomeComponent, 
    SidebarComponent, 
    ViewPostComponent, 
    CreatePostComponent, 
    QuizWelcomeComponent, 
    QuestionDisplayComponent, 
    ChangeBgDirective, 
    SearchPostComponent, 
    ProfileComponent, 
    DisplayPostsComponent, 
    DisplayCommentsComponent, 
    DisplayFavComponent
  ],
  imports: [
    BrowserModule, FormsModule, ReactiveFormsModule, 
    BrowserAnimationsModule, MaterialModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes, { useHash: true}),
    NgxWebstorageModule.forRoot(),
    ToastrModule.forRoot(),
    FontAwesomeModule,
    EditorModule
  ],
  providers: [AuthService, PostService, CommentService, VoteService, QuizService, AuthGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }, 
    { provide: JWT_OPTIONS, useValue: JWT_OPTIONS },
        JwtHelperService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
