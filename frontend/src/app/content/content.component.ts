import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import * as $ from 'jquery';
import * as uuid from 'uuid';
import * as parse from 'parse-duration';
import { Content, Extend } from '../protocols/model';
import { BackendService } from '../backend.service'
import { NGXLogger } from 'ngx-logger';

declare var $: any;

@Component({
  selector: 'app-content',
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.scss']
})
export class ContentComponent implements OnInit {

  content: Content;

  prepTime: string;
  cookTime: string;

  constructor(
    private backendService: BackendService,
    private location: Location,
    private route: ActivatedRoute,
    private logger: NGXLogger) {
  }

  newContent(): Content {
    this.content = new Content();
    this.content.id = uuid.v4(); // UUID random
    this.content.extend = new Extend();
    return this.content;
  }

  getContent(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id == null) {
      this.newContent();
    } else {
      this.backendService.getContent(id)
        .subscribe(returned => {
          this.content = returned;
          this.logger.trace('getContent:', this.content);
        });
    }
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    this.backendService.updateContent(this.content)
      .subscribe(() => this.goBack());
  }

  ngOnInit() {
    this.getContent();

    $(document).ready(function() {
      $('#directions').summernote({
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
