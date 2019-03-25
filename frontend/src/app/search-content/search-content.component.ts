import { Component, OnInit } from '@angular/core';
import { filter } from 'rxjs/operators';

import { Content } from '../_models';
import { BackendService } from '../_services'
import { Config, Model } from '../config';
import { NGXLogger } from 'ngx-logger';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-search-content',
  templateUrl: './search-content.component.html',
  styleUrls: ['./search-content.component.scss']
})
export class SearchContentComponent implements OnInit {

  contents: Content[];
  nextInfo: string;
  getNext: () => void;

  imgServerUrl: string = Config.gatewayUrl;

  constructor(
    private backendService: BackendService,
    private route: ActivatedRoute,
    private logger: NGXLogger
  ) { }

  ngOnInit() {
    this.route.params.subscribe(param => {
      this.logger.trace('ActivatedRoute params subscrive:', param);
      let tag: string = param['tag'];
      this.contents = new Array<Content>();
      if (tag) {
        this.search(tag);
      } else {
        this.getPopularContents();
      }
    });
  }

  //TODO get container id from the backend
  getPopularContents(): void {
    this.getNext = this.getNextLatest;
    this.backendService.getPopularItems<Content>(Model.Content, "56a1cd50-3c77-11e9-bf26-d571c84212ed")
        .subscribe(contents => {
          this.contents = this.contents.concat(contents);
        });

    this.getNextLatest();
  }

  getNextLatest() {
    this.backendService.getModels<Content>(Model.Content, this.nextInfo, 'cid', "56a1cd50-3c77-11e9-bf26-d571c84212ed")
        .pipe(filter(Boolean))
        .subscribe(respWithLink => {
          this.contents = this.contents.concat(respWithLink.response);
          this.nextInfo = respWithLink.getNextQueryParams();
          this.logger.trace("nextQueryParams:", this.nextInfo);
        });
  }

  search(tag?: string) {
    this.getNext = this.getNextSearch;
    this.getNextSearch(tag);
  }

  getNextSearch(tag?: string) {
    this.backendService.getSearch(this.nextInfo, tag)
        .pipe(filter(Boolean))
        .subscribe(respWithLink => {
          this.contents = this.contents.concat(respWithLink.response);
          this.nextInfo = respWithLink.getNextQueryParams();
          this.logger.trace("nextQueryParams:", this.nextInfo);
        });
  }

}
