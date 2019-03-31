import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Content, IdCid, User } from '../_models';
import { BackendService } from '../_services';
import { Config } from '../config';
import { NGXLogger } from 'ngx-logger';

@Component({
  selector: 'app-content-viewer',
  templateUrl: './content-viewer.component.html',
  styleUrls: ['./content-viewer.component.scss']
})
export class ContentViewerComponent implements OnInit {

  user: User;

  content: Content;

  constructor(
    private backendService: BackendService,
    private location: Location,
    private route: ActivatedRoute,
    private logger: NGXLogger) { }

  ngOnInit() {
    this.backendService.getUser().subscribe(user => this.user = user);

    let identity = this.route.snapshot.paramMap.get('id');
    let containerId = this.route.snapshot.paramMap.get('cid');

    if (identity && containerId) {
      this.backendService.getModel<Content>(Content, IdCid.of(identity, containerId))
          .toPromise()
          .then(content => {
            this.content = content;
            this.logger.trace('ContentViewer, content:', content);
          });
    }
  }


}
