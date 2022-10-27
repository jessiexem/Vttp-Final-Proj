import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header.component';
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
//import { VoteButtonComponent } from './shared/vote-button/vote-button.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { VoteService } from './shared/vote-button/vote.service';
import { EditorModule } from '@tinymce/tinymce-angular';


const appRoutes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'view-post/:id', component: ViewPostComponent },
  { path: '**', redirectTo: '/',pathMatch: 'full'}
]

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    LoginComponent,
    SignupComponent,  
    HomeComponent, SidebarComponent, ViewPostComponent
    // , VoteButtonComponent
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
  providers: [AuthService, PostService, CommentService, VoteService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
