import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
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

  @ViewChild('toUpload')
  toUpload!: ElementRef

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
      tags: this.fb.control<Tag[]>([], Validators.required),
      file: this.fb.control<any>('')
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
    const postName = this.createPostForm.get('postName')?.value
    const description = this.createPostForm.get('description')?.value
    const tagValues = this.tags.map((obj) => {
      return obj.tag;
    })

    const tags = String(tagValues)
    //console.log("tagValues:", tagValues, "tags:",tags)

    console.info(">>>toUpload:", this.toUpload.nativeElement.files)
    const file = this.toUpload.nativeElement.files[0]

    const payload = <CreatePostPayload> ({
      postName: postName,
      description: description,
      tags: tags
    })
    this.postService.createPost(payload, file)
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
