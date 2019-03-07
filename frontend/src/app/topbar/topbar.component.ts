import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NGXLogger, LoggerConfig } from 'ngx-logger';

@Component({
  selector: 'app-topbar',
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.scss']
})
export class TopbarComponent implements OnInit {

  searchTag: string;

  constructor(
    private router: Router,
    private logger: NGXLogger) {

  }

  ngOnInit() {
  }

  onSearch() {
    this.logger.trace("onSearch(), searchTag", this.searchTag);
    if (this.searchTag) {
      this.router.navigate(['/', 'search', this.searchTag]);
    }
  }

}
