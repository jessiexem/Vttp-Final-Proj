import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { LocalStorageService } from 'ngx-webstorage';
import { AuthService } from './auth.service';

@Injectable()
export class AuthGuardService implements CanActivate {
    
  isLoggedIn!: boolean;
  
  constructor(public auth: AuthService, public router: Router, private localStorage: LocalStorageService) {}

  canActivate(): boolean {
    if (!this.auth.isAuthenticated()) {

      this.localStorage.clear('authenticationToken');
      this.localStorage.clear('username');
      this.localStorage.clear('refreshToken');
      this.localStorage.clear('expiresAt');
      this.localStorage.clear('dpUrl')

      this.isLoggedIn = false;

      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }
}