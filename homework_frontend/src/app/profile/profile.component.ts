import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { AuthService } from '../auth/shared/auth.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  
  // @ViewChild('toUpload')
  // toUpload!: ElementRef

  url!: string
  
  constructor(private authSvc: AuthService, private localStorage: LocalStorageService) { }

  ngOnInit(): void {
    this.url = this.localStorage.retrieve('dpUrl')
    console.log("dpUrl:",this.url)
  }

  onSelectFile(event: any) {
    //const file = this.toUpload.nativeElement.files[0]
    // console.log(event.type)
    if (event.target.files && event.target.files[0]) {
      const file = event.target.files[0];
      this.authSvc.uploadProfilePic(file)
      .then(
        result => {
          console.log("update display pic result:", result)
          //console.log("dp url:", result.profilePicUrl)
          this.url = this.localStorage.retrieve('dpUrl')
        }
      )
      .catch(
        error => {
          console.error("update display pic: createPost error:", error)
        }
      )
    }

  }
}
