import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { SignupRequest } from 'src/app/models';
import { AuthService } from '../shared/auth.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  signupForm!: FormGroup;

  constructor(private fb: FormBuilder, private authSvc : AuthService, 
    private toastr: ToastrService, private router: Router) {}
  

  ngOnInit(): void {
    this.signupForm = this.fb.group ({
      email: this.fb.control<string>('', [Validators.required, Validators.email]),
      username: this.fb.control<string>('', [Validators.required, Validators.minLength(5)]),
      password: this.fb.control<string>('',[Validators.required])
    })
  }

  signup() {
    const signupReq = this.signupForm.value as SignupRequest

    this.authSvc.signup(signupReq)
    .then(result => {
      console.info('>>>> Signup result:', result)
      this.router.navigate(['/login'], { queryParams: { registered: 'true'}})

    })
    .catch(error => {
      console.error("--- Signup error:", error)
      this.toastr.error('Registration failed! Please try again.')
      //alert(`Cannot signup ${signupReq.username}. Please try again.`)
    })
  }
}
