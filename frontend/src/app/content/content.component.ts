import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
import * as $ from 'jquery';
import { Content, Extend, Review, Reputation } from '../protocols/model';
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

  ngOnInit() {
    this.prepareContent();
  }

  prepareContent(): void {
    let observable = this.id ? this.backendService.getContent(this.id) : this.newContent();
    observable.subscribe(c => {
      this.content = c;
      this.convertFromContent();
      this.sentences();
      this.logger.trace('getContent:', c);
    });
  }

  newContent(): Observable<Content> {
    this.content = new Content();
    return of(this.content);
  }

  convertFromContent(): void {
    for (let i in this.content.extend.ingredients) {
      this.flatIngredients += this.content.extend.ingredients[i] + '\n';
    }
  }

  convertToContent(): void {
    this.content.extend.ingredients = this.flatIngredients.split('\n');
  }

  sentences(): void {
    let component = this;

    $(document).ready(function() {
      if(component.isReadonly) {
        // $('#directions').summernote({
        //   airMode: true,
        // });
        // $('#directions').summernote('insertText', component.content.sentences);
        $('#directions').html(component.content.sentences);
      } else {
        $('#directions').summernote({
          // height: 300,
          toolbar: [
            ['style', ['style']],
            ['font', ['bold', 'italic', 'underline', 'clear']],
            ['fontColor', ['color']],
            ['history', ['undo', 'redo']],
            ['para', ['ul', 'paragraph', 'height']],
            ['insert', ['link', 'picture', 'video']],
            ['fullscreen', ['fullscreen']]
          ]
        });
      }
    });
  }

  onSave(): void {
    this.convertToContent();
    this.logger.trace('onSave:', this.content);
    this.backendService.addContent(this.content)
      .subscribe(() => {});
  }

  styleSuffix(): string {
    return this.isReadonly ? "Readonly": "";
  }

  goBack(): void {
    this.location.back();
  }

}
