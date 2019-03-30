import { Component, OnInit } from '@angular/core';
import { filter, concatMap } from 'rxjs/operators';

import { Content, WebSite } from '../_models';
import { BackendService } from '../_services'
import { Config } from '../config';
import { NGXLogger, LoggerConfig } from 'ngx-logger';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-search-content',
  templateUrl: './search-content.component.html',
  styleUrls: ['./search-content.component.scss']
})
export class SearchContentComponent implements OnInit {

  webSite: WebSite;

  contents: Content[] = new Array<Content>();
  nextInfo: string;
  getNext: () => Promise<Content[]>;

  imgServerUrl: string = Config.gatewayUrl;

  constructor(
    private backendService: BackendService,
    private route: ActivatedRoute,
    private logger: NGXLogger
  ) { }

  ngOnInit() {
    this.fetchWebSite()
      .then(() => this.initContents().toPromise());
  }

  fetchWebSite(): Promise<WebSite> {
    return this.backendService.getWebSite().toPromise().then(webSite => this.webSite = webSite);
  }

  initContents(): Observable<Content[]> {
    return this.route.params.pipe(
      concatMap(param => {
        this.logger.trace('ActivatedRoute params:', param);
        let tag: string = param['tag'];
        if (tag) {
          return this.getNextSearch(tag);
        } else {
          return this.getPopularContents().then(() => this.getNextLatest());
        }
      })
    );
  }

  getPopularContents(): Promise<Content[]> {
    return this.backendService.getPopularItems<Content>(
      Content,
      this.webSite.contentContainerIds[Config.RECIPE]
    ).toPromise().then(contents => {
      this.contents = this.contents.concat(contents);
      return this.contents;
    });
  }

  getNextLatest(): Promise<Content[]> {
    this.getNext = this.getNextLatest;
    return this.backendService.getModels<Content>(Content, this.nextInfo,
        'cid', this.webSite.contentContainerIds[Config.RECIPE])
        .pipe(filter(Boolean))
        .toPromise()
        .then(respWithLink => {
          this.contents = this.contents.concat(respWithLink.response);
          this.nextInfo = respWithLink.getNextQueryParams();
          this.logger.trace("nextQueryParams:", this.nextInfo);
          return this.contents;
        });
  }

  getNextSearch(tag?: string): Promise<Content[]> {
    this.getNext = this.getNextSearch;
    return this.backendService.getSearch(this.nextInfo, tag)
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
