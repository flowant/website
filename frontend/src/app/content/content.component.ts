import { Component, OnInit } from '@angular/core';
import * as $ from 'jquery';
import { Content } from '../protocols/model';
import { BackendService } from '../backend.service'

declare var $: any;

@Component({
  selector: 'app-content',
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.scss']
})
export class ContentComponent implements OnInit {

  contents: Content[] = [];

  content: Content;

  constructor(private backendService: BackendService) { }

  getContents(): void {
    this.backendService.getContents()
      .subscribe(contents => this.content = contents[0]);
  }

  test(): void {
    this.content.id;
  }

  ngOnInit() {
    this.getContents();

    $(document).ready(function() {
      $('.summernote.directions').summernote({
        placeholder: 'Please type directions here.',
        height: 200,
        // airMode: true,
        // focus: true
        toolbar: [
          // [groupName, [list of button]]
          ['style', ['style']],
          ['font', ['bold', 'italic', 'underline', 'clear']],
          ['fontColor', ['color']],
          ['history', ['undo', 'redo']],
          ['para', ['ul', 'paragraph', 'height']],
          ['insert', ['link', 'picture', 'video']],
          ['fullscreen', ['fullscreen']]
        ]
      });
    });

  }

  // TODO destroy after using it
      // $('#summernote').summernote('destroy');
}
