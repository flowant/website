import { Component, OnInit, Input } from '@angular/core';
import { IdCid, Review } from '../protocols/model';
import { BackendService } from '../backend.service'
import { Config, Model } from '../config';
import { NGXLogger } from 'ngx-logger';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.scss']
})
export class ReviewComponent implements OnInit {

  @Input() idCid: IdCid;
  test:string = "width:20%";

  reviews : Review[];

  ratingSelected = 0;

  constructor(private backendService: BackendService, private logger: NGXLogger) { }

  ngOnInit() {
    this.getReviews();
  }

  getReviews(): void {
    this.backendService.getPopularItems<Review>(Model.Review, this.idCid.identity)
      .subscribe(returned => this.reviews = returned);
  }

}
