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
  getNext: () => void = this.getNextLatest;

  imgServerUrl: string = Config.gatewayUrl;

  constructor(
    private backendService: BackendService,
    private route: ActivatedRoute,
    private logger: NGXLogger
  ) { }

  ngOnInit() {
    return this.fetchWebSite()
        .then(() => this.init().toPromise().then());
  }

  fetchWebSite(): Promise<WebSite> {
    return this.backendService.getWebSite().toPromise().then(webSite => this.webSite = webSite);
  }

  init(): Observable<Content[]> {
    return this.route.params.pipe(
      concatMap(param => {
        this.logger.trace('ActivatedRoute params:', param);
        let tag: string = param['tag'];
        if (tag) {
          return this.search(tag);
        } else {
          return this.getPopularContents();
        }
      }));
  }

  getPopularContents(): Promise<Content[]> {
    return this.backendService.getPopularItems<Content>(
      Content,
      this.webSite.contentContainerIds[Config.RECIPE]
    ).toPromise().then(contents => {
      this.contents = this.contents.concat(contents);
      return this.contents;
    }).then(() => this.getNextLatest());
  }

  getNextLatest(): Promise<Content[]> {
    return this.backendService.getModels<Content>(
      Content,
      this.nextInfo,
      'cid',
      this.webSite.contentContainerIds[Config.RECIPE]
    ).pipe(filter(Boolean)).toPromise().then(respWithLink => {
      this.contents = this.contents.concat(respWithLink.response);
      this.nextInfo = respWithLink.getNextQueryParams();
      this.logger.trace("nextQueryParams:", this.nextInfo);
      return this.contents;
    });
  }

  search(tag?: string): Promise<Content[]> {
    this.getNext = this.getNextSearch;
    return this.getNextSearch(tag);
  }

  getNextSearch(tag?: string): Promise<Content[]> {
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
