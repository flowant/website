import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
import * as $ from 'jquery';
import { Content, IdCid, FileRefs, Extend, Review, Reputation } from '../protocols/model';
import { BackendService } from '../backend.service';
import { Config, Model } from '../config';
import { NGXLogger } from 'ngx-logger';

declare var $: any;

@Component({
  selector: 'app-content',
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.scss']
})
export class ContentComponent implements OnInit {

  imgServerUrl: string = Config.gatewayUrl;
  content: Content;

  isReadonly: boolean;
  idCid: IdCid;

  flatIngredients: string = "";

  constructor(
    private backendService: BackendService,
    private location: Location,
    private route: ActivatedRoute,
    private logger: NGXLogger) {

    this.idCid = new IdCid(this.route.snapshot.paramMap.get('id'),
        this.route.snapshot.paramMap.get('cid'));

    // TODO should depend on permission
    this.isReadonly = this.idCid != null;
  }

  ngOnInit() {
    this.prepareContent();
  }

  prepareContent(): void {
    let observable = this.idCid ? this.backendService.getModel<Content>(Model.Content, this.idCid) : this.newContent();
    observable.subscribe(c => {
      this.content = c;
      this.convertFromContent();
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
    this.renderSentences();
  }

  convertToContent(): void {
    this.content.extend.ingredients = this.flatIngredients.split('\n');
    this.content.sentences = $('#directions').summernote('code');
  }

  renderSentences(): void {
    let component = this;

    $(document).ready(function() {
      if(component.isReadonly) {
        $('#directions').html(component.content.sentences);
      } else {
        $('#directions').summernote({
          placeholder: component.content.sentences,
          toolbar: [
            ['style', ['style']],
            ['font', ['bold', 'italic', 'underline', 'clear']],
            ['fontColor', ['color']],
            ['history', ['undo', 'redo']],
            ['para', ['ul', 'paragraph', 'height']],
            ['insert', ['link', 'picture', 'video']],
            ['fullscreen', ['fullscreen']]
          ],
          callbacks: {
            onImageUpload: function(files) {
              component.backendService.addFiles(files).subscribe(fileRefs => {
                for(let fileRef of fileRefs) {
                  component.content.fileRefs.push(fileRef);
                  $('#directions').summernote('insertImage', component.imgServerUrl + fileRef.uri);
                }
              });
            },
            onMediaDelete : function (target) {
              // This callback isn't called when Media are deleted by a keyboard.
              // We need another way to delete images on the backend. see deleteUnusedFile()
              // Use this event handler after this issue is resolved.
              // component.logger.trace('onMediaDelete:', target.attr('src'));
            }
          }
        });
      }
    });
  }

  // see summernote's onMediaDelete event handler
  deleteUnusedFile() {
    if (this.content.fileRefs.length == 0) {
      return;
    }

    const [used, unused] =
        this.content.fileRefs.reduce((result, element) => {
          result[this.content.sentences.indexOf(element.uri) !== -1 ? 0 : 1].push(element);
          return result;
        }, [[], []]);
    this.content.fileRefs = used;

    if (unused.length > 0) {
      this.backendService.deleteFiles(unused)
          .subscribe(() => {});
    }
  }

  onSave(): void {
    this.convertToContent();
    this.deleteUnusedFile();
    this.logger.trace('onSave:', this.content);
    this.backendService.postModel<Content>(Model.Content, this.content)
      .subscribe(() => {});
  }

  onDelete(): void {
    this.logger.trace('onDelete:', this.content);
    this.backendService.deleteModel<Content>(Model.Content, this.content)
      .subscribe(() => {});
  }

  styleSuffix(): string {
    return this.isReadonly ? "Readonly": "";
  }

  goBack(): void {
    this.location.back();
  }

  getRated(): string {
    let avr = this.content.reputation.rated / this.content.reputation.reputed;
    return avr.toFixed(1);
  }

  onRepute(content: Content, selected: string) {
    this.backendService.onRepute(Model.Content, content.idCid, selected)
        .subscribe((rpt) => {content.reputation = rpt});
  }

}
