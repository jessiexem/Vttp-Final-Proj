import { Component, OnInit, Output } from '@angular/core';
import { Subject } from 'rxjs';
import { PostService } from 'src/app/auth/shared/post.service';

@Component({
  selector: 'app-search-post',
  templateUrl: './search-post.component.html',
  styleUrls: ['./search-post.component.css']
})
export class SearchPostComponent implements OnInit {

  searchValue = ''

  @Output()
  onSearchPost = new Subject<string>()

  constructor(private postSvc: PostService) { }

  ngOnInit(): void {
  }

  search() {
    console.log("---search:" ,this.searchValue);
    
    this.onSearchPost.next(this.searchValue) //let Home component do the search

  }
}
