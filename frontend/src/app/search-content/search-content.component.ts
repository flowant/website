import { Component, OnInit } from '@angular/core';
import { Content, Extend } from '../protocols/model';
import { BackendService } from '../backend.service'
import { Config, Model } from '../config';
import { NGXLogger, LoggerConfig } from 'ngx-logger';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-search-content',
  templateUrl: './search-content.component.html',
  styleUrls: ['./search-content.component.scss']
})
export class SearchContentComponent implements OnInit {

  contents : Content[] = new Array<Content>();
  nextInfo: string;
  tag: string;
  getNext: () => void;

  constructor(
      private backendService: BackendService,
      private route: ActivatedRoute,
      private logger: NGXLogger) {

    this.tag = this.route.snapshot.paramMap.get('tag');
    this.getNext = this.tag ? this.getNextSearch : this.getNextContent;
  }

  ngOnInit() {
    if (this.tag) {
      this.search(this.tag);
    } else {
      this.getPopularContents();
    }
  }

  getPopularContents(): void {
    this.backendService.getPopularItems<Content>(Model.Content, "56a1cd50-3c77-11e9-bf26-d571c84212ed")
        .subscribe(contents => {
          this.contents = this.contents.concat(contents);
        });

    this.getNextContent();
  }

  getNextContent() {
    this.backendService.getModels<Content>(Model.Content, this.nextInfo, "56a1cd50-3c77-11e9-bf26-d571c84212ed")
        .subscribe(respWithLink => {
          this.contents = this.contents.concat(respWithLink.response);
          this.nextInfo = respWithLink.getNextQueryParams();
          this.logger.trace("nextQueryParams:", this.nextInfo);
        });
  }

  search(tag?: string) {
    this.tag = tag ? tag : this.tag;
    this.getNext = this.getNextSearch;
    this.getNextSearch();
  }

  getNextSearch() {
    this.backendService.getSearch(this.nextInfo, this.tag)
        .subscribe(respWithLink => {
          this.contents = this.contents.concat(respWithLink.response);
          this.nextInfo = respWithLink.getNextQueryParams();
          this.logger.trace("nextQueryParams:", this.nextInfo);
        });
  }

}
