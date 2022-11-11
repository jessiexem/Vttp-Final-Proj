import { ChangeDetectorRef, Component, ViewChild } from '@angular/core';
import { AuthService } from './auth/shared/auth.service';
import { MatDrawer } from '@angular/material/sidenav';
import { Router } from '@angular/router';
import { LocalStorageService } from 'ngx-webstorage';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  isLoggedIn!: boolean;
  username!: string;
  url!: string;

  sub$!: Subscription

  @ViewChild(MatDrawer)
  drawer!: MatDrawer

  constructor(private authService: AuthService, private router: Router, private localStorage: LocalStorageService) { }

  ngOnInit(): void {
    this.authService.onLoggedIn.subscribe((data: boolean) => this.isLoggedIn = data);
    this.authService.username.subscribe((data: string) => this.username = data);
    this.isLoggedIn = this.authService.isLoggedIn();
    this.username = this.authService.getUserName();
    
    //this.sub$ = this.authService.dpUrl.subscribe((data: string) => this.url = data)
    this.authService.dpUrl.subscribe((data: string) => this.url = data)
    this.url = this.localStorage.retrieve('dpUrl')

  }

  logout() {
    this.authService.logout();
    this.isLoggedIn = false;
    this.url = '';
    this.router.navigateByUrl('/login');
  }

  toggleSidebar() {
    console.log(">>toggle sidebar:",this.drawer)
    this.drawer.toggle()
  }

}
