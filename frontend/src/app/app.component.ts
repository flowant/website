import { Component } from '@angular/core';
import { NgbRatingConfig } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger, LoggerConfig } from 'ngx-logger';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [NgbRatingConfig]
})
export class AppComponent {

  constructor(
    ratingConfig: NgbRatingConfig,
    private logger: NGXLogger) {

    // customize default values of ratings used by this component tree
    ratingConfig.max = 5;
  }

  ngOnInit() {
  }

}
