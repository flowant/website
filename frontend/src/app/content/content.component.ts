import { Component, OnInit } from '@angular/core';
import * as $ from 'jquery';

declare var $: any;

@Component({
  selector: 'app-content',
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.css']
})
export class ContentComponent implements OnInit {

  constructor() { }

  ngOnInit() {
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
