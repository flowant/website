import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
import { tap, concatMap } from 'rxjs/operators';
import * as $ from 'jquery';
import { Content, IdCid, User, WebSite } from '../_models';
import { BackendService } from '../_services';
import { Config } from '../config';
import { NGXLogger } from 'ngx-logger';

declare var $: any;

@Component({
  selector: 'app-content-editor',
  templateUrl: './content-editor.component.html',
  styleUrls: ['./content-editor.component.scss']
})
export class ContentEditorComponent implements OnInit {

  imgServerUrl: string = Config.gatewayUrl;

  containerId: string;

  user: User;

  content: Content;

  idCid: IdCid;

  flatIngredients: string = "";

  constructor(
    private backendService: BackendService,
    private location: Location,
    private router: Router,
    private route: ActivatedRoute,
    private logger: NGXLogger) { }

  ngOnInit() {
    let identity = this.route.snapshot.paramMap.get('id');
    let containerId = this.route.snapshot.paramMap.get('cid');

    if (identity && containerId) {
      this.idCid = IdCid.of(identity, containerId);
    }

    this.backendService.getWebSite().pipe(
      tap(webSite => this.containerId = webSite.contentContainerIds[Config.RECIPE]),
      concatMap(_ => this.backendService.getUser()),
      tap(user => this.user = user),
      concatMap(user => this.prepareContent(user))
    ).toPromise();
  }

  prepareContent(user: User): Observable<Content> {
    let oContent: Observable<Content> =
        this.idCid ? this.backendService.getModel<Content>(Content, this.idCid) : of(Content.of(user, this.containerId));

    return oContent.pipe(
      tap(c => {
        if (Boolean(c) === false) {
          return this.notFound();
        }
        this.content = c;
        this.idCid = c.idCid;
        this.convertFromContent();
      })
    );
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
      $('#directions').summernote({
        placeholder: 'Please type directions here.',
        height: 200,
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
    });
  }

  // see summernote's onMediaDelete event handler
  deleteUnusedFile() {
    if (!this.content.fileRefs || this.content.fileRefs.length == 0) {
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
          .toPromise();
    }
  }

  isValid(...args: string[]): boolean {
    return args.every(Boolean);
  }

  onSave(goToView: boolean): void {
    this.convertToContent();
    this.deleteUnusedFile();
    this.logger.trace('onSave:', this.content);
    this.backendService.postModel<Content>(Content, this.content)
      .toPromise()
      .then(_ => {
        if(goToView) {
          this.router.navigate(['/content/view/', this.idCid.identity, this.idCid.containerId]);
        }
      });
  }

  notFound(): Promise<boolean> {
    return this.router.navigate(['/', 'page-not-found']);
  }

  onDelete(): void {
    this.logger.trace('onDelete:', this.content);
    this.backendService.deleteModel(Content, this.content.idCid)
      .toPromise()
      .then(() => this.router.navigate(['/user/content/', this.user.identity]));
  }

}
