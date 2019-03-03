import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {NgbRatingConfig} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [NgbRatingConfig]
})
export class AppComponent {
  constructor(
    private http: HttpClient,
    ratingConfig: NgbRatingConfig) {

    // customize default values of ratings used by this component tree
    ratingConfig.max = 5;
  }
}
