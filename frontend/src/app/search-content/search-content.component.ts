import { Component, OnInit } from '@angular/core';
import { Content, Extend } from '../protocols/model';
import { BackendService } from '../backend.service'
import { Config, Model } from '../config';
import { NGXLogger } from 'ngx-logger';

@Component({
  selector: 'app-search-content',
  templateUrl: './search-content.component.html',
  styleUrls: ['./search-content.component.scss']
})
export class SearchContentComponent implements OnInit {

  contents : Content[];

  constructor(private backendService: BackendService, private logger: NGXLogger) { }

  ngOnInit() {
    this.getContents();
  }

  getContents(): void {
    this.backendService.getPopularItems<Content>(Model.Content, "56a1cd50-3c77-11e9-bf26-d571c84212ed")
      .subscribe(returned => this.contents = returned);
  }
}
