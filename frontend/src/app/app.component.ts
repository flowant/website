import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NgbRatingConfig } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger, LoggerConfig } from 'ngx-logger';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [NgbRatingConfig]
})
export class AppComponent {

  searchTag: string;

  constructor(
    private http: HttpClient,
    ratingConfig: NgbRatingConfig,
    private router: Router,
    private logger: NGXLogger) {

    // customize default values of ratings used by this component tree
    ratingConfig.max = 5;
  }

  ngOnInit() {
  }

  onSearch() {
    this.logger.trace("onSearch(), searchTag", this.searchTag);
    // TODO how to search again after search component is initialized.
    // via service?
    if (this.searchTag) {
      this.router.navigate(['/', 'search', this.searchTag]);
    }
  }

}
