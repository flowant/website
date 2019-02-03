import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import * as $ from 'jquery';
import * as uuid from 'uuid';
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

  isReadonly: boolean;
  id: string;

  flatIngredients: string = "";

  constructor(
    private backendService: BackendService,
    private location: Location,
    private route: ActivatedRoute,
    private logger: NGXLogger) {

    this.id = this.route.snapshot.paramMap.get('id');
    // TODO should depend on permission
    this.isReadonly = this.id != null;
  }

  styleSuffix(): string {
    return this.isReadonly ? "Readonly": "";
  }

  newContent(): Content {
    this.content = new Content();
    this.content.id = uuid.v4(); // UUID random
    this.content.extend = new Extend();
    return this.content;
  }

  getContent(): void {
    if (this.id == null) {
      this.newContent();
    } else {
      this.backendService.getContent(this.id)
          .subscribe(returned => {
            this.convertFromContent(returned);
            this.logger.trace('getContent:', returned);
            // this content should be set lastly
            this.content = returned;
          });
    }
  }

  convertFromContent(content: Content): void {
    for (let ingredient of content.extend.ingredients) {
      this.flatIngredients += ingredient + '\n';
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
      // TODO destroy after using it
      // $('#summernote').summernote('destroy');
    });
  }
}
