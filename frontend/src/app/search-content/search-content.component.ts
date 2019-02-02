import { Component, OnInit } from '@angular/core';
import { Content, Extend } from '../protocols/model';
import { BackendService } from '../backend.service'
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
    this.backendService.getContents()
      .subscribe(returned => this.contents = returned);
  }
}
