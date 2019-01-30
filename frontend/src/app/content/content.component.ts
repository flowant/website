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
      $('#summernote').summernote({
        placeholder: 'Hello bootstrap 4',
        tabsize: 2,
        height: 100,
        focus: true
      });
      // TODO destroy after using it
      // $('#summernote').summernote('destroy');
    });
  }

}
