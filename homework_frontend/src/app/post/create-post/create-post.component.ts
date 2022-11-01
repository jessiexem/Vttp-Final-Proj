import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatChipInputEvent } from '@angular/material/chips';
import { Router } from '@angular/router';
import { PostService } from 'src/app/auth/shared/post.service';
import { CreatePostPayload, Tag } from 'src/app/models';
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.css']
})
export class CreatePostComponent implements OnInit {

  addOnBlur = true;
  isEditorContentEmpty = true;
  readonly separatorKeysCodes = [ENTER, COMMA] as const;
  tags: Tag[] = []

  createPostForm!: FormGroup;

  constructor(private router: Router, private postService: PostService, private fb: FormBuilder, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.createPostForm = this.createForm()
  }

  createForm() : FormGroup {
    return this.fb.group ({
      postName: this.fb.control<string>('', [Validators.required]),
      description: this.fb.control<string>('', [Validators.required]),
      tags: this.fb.control<Tag[]>([], Validators.required)
    })
  }

  validate () {
    if (this.createPostForm.value['description'] == '') {
      this.isEditorContentEmpty = true
    } else {
      this.isEditorContentEmpty = false
    }
  }

  createPost() {
    const payload = this.createPostForm.value as CreatePostPayload
  
    const tagValues = this.tags.map((obj) => {
      return obj.tag;
    })

    console.log(tagValues)
    payload.tags = tagValues

    this.postService.createPost(payload)
    .then(
      result => {
        console.log("Create Post: createPost result:", result)
        this.toastr.success("Your post has been created successfully.")
        this.router.navigateByUrl('/')
      }
    )
    .catch(
      error => {
        console.error("Create Post: createPost error:", error)
        this.toastr.error("Your post could not be saved.")
      }
    )

    this.createPostForm.reset()
  }


  discardPost() {
    this.router.navigateByUrl('/');
  }

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    // Add our fruit
    if (value) {
      this.tags.push({tag: value});
    }

    // Clear the input value
    event.chipInput!.clear();
  }

  remove(tag: Tag): void {
    const index = this.tags.indexOf(tag);

    if (index >= 0) {
      this.tags.splice(index, 1);
    }
  }

}
