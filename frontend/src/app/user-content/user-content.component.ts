import { Component, OnInit } from '@angular/core';
import { Content } from '../_models';
import { BackendService } from '../_services';
import { Router, ActivatedRoute } from '@angular/router';
import { NGXLogger } from 'ngx-logger';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-user-content',
  templateUrl: './user-content.component.html',
  styleUrls: ['./user-content.component.scss']
})
export class UserContentComponent implements OnInit {

  contents: Content[] = new Array<Content>();
  nextInfo: string;
  identity: string;

  constructor(
    private backendService: BackendService,
    private route: ActivatedRoute,
    private router: Router,
    private logger: NGXLogger) { }

  ngOnInit() {
    this.identity = this.route.snapshot.paramMap.get('id');
    if (this.identity) {
      this.getNext();
    }
  }

  getNext(): Promise<Content[]> {
    return this.backendService.getModels<Content>(Content, this.nextInfo, { aid: this.identity })
        .pipe(filter(Boolean))
        .toPromise()
        .then(respWithLink => {
          this.contents = this.contents.concat(respWithLink.response);
          this.nextInfo = respWithLink.getNextQueryParams();
          this.logger.trace("nextQueryParams:", this.nextInfo);
          return this.contents;
        });
  }

}
