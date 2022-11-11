import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LoginRequest } from 'src/app/models';
import { AuthService } from '../shared/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  registerSuccessMessage!: string
  isError!: boolean

  loginForm!: FormGroup;

  constructor(private fb: FormBuilder, private authSvc : AuthService,
     private ar: ActivatedRoute, private router: Router,
     private toastr: ToastrService) { }

  ngOnInit(): void {
    this.loginForm = this.fb.group ({
      username: this.fb.control<string>('', [Validators.required, Validators.minLength(5)]),
      password: this.fb.control<string>('',[Validators.required])
    })

    if ( this.ar.snapshot.queryParams['registered'] !== undefined && this.ar.snapshot.queryParams['registered'] === 'true') {
      this.toastr.success('Signup Successful')
      this.registerSuccessMessage = "Please check your inbox for activation email. Please activate your account before you login."
    }
  }

  login() {
    const loginReq = this.loginForm.value as LoginRequest

    this.authSvc.login(loginReq)
    .then(result => {
      console.info('>>>> Login successful:', result)
      this.isError = false
      this.router.navigate(['/'])
      this.toastr.success('Login Successful')
    })
    .catch(error => {
      console.error("---Login error:", error)
      this.isError = true
      //alert(`Cannot login ${loginReq.username}. Please try again.`)
    })
  }

}
