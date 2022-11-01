// import { Component, OnInit, ViewChild } from '@angular/core';
// import { Router } from '@angular/router';
// import { AuthService } from '../auth/shared/auth.service';
// import { MatDrawer } from '@angular/material/sidenav';

// @Component({
//   selector: 'app-header',
//   templateUrl: './header.component.html',
//   styleUrls: ['./header.component.css']
// })
// export class HeaderComponent implements OnInit {

//   isLoggedIn!: boolean;
//   username!: string;

//   @ViewChild(MatDrawer)
//   drawer!: MatDrawer

//   constructor(private authService: AuthService, private router: Router) { }

//   ngOnInit(): void {
//     this.authService.onLoggedIn.subscribe((data: boolean) => this.isLoggedIn = data);
//     this.authService.username.subscribe((data: string) => this.username = data);
//     this.isLoggedIn = this.authService.isLoggedIn();
//     this.username = this.authService.getUserName();
//   }

//   logout() {
//     this.authService.logout();
//     this.isLoggedIn = false;
//     this.router.navigateByUrl('/login');
//   }

//   toggleSidebar() {
//     console.log(">>toggle sidebar:",this.drawer)
//     this.drawer.toggle()
//   }
// } 
