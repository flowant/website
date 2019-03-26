import { Component, OnInit, Input } from '@angular/core';
import { IdCid, Content, Review, Reputation, User } from '../_models';
import { BackendService } from '../_services'
import { NGXLogger } from 'ngx-logger';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.scss']
})
export class ReviewComponent implements OnInit {

  @Input() content: Content;

  user: User;

  review: Review;

  reviews: Review[] = new Array<Review>();

  nextInfo: string;

  postedRpt: Reputation;

  constructor(
    private backendService: BackendService,
    private logger: NGXLogger) { }

  ngOnInit() {
    if (this.content) {
      this.review = new Review();
      this.review.idCid = IdCid.random(this.content.idCid.identity);
      this.getPopularReviews();
    }
    this.backendService.getUser().subscribe(u => this.user = u);
  }

  getPopularReviews(): void {
    this.backendService.getPopularItems<Review>(Review, this.content.idCid.identity)
      .subscribe(pupolarReviews => {
        this.reviews = this.reviews.concat(pupolarReviews);
        this.getNextReview();
      });
  }

  getNextReview() {
    this.backendService.getModels<Review>(Review, this.nextInfo, 'cid', this.content.idCid.identity)
        .subscribe(respWithLink => {
          this.reviews = this.reviews.concat(respWithLink.response);
          this.nextInfo = respWithLink.getNextQueryParams();
          this.logger.trace("nextInfo:", this.nextInfo);
        });
  }

  onSave(): void {
    // check user id and already clicked by user

    this.logger.trace('onSave:', this.review);
    this.backendService.postModel<Review>(Review, this.review)
        .subscribe(() => {});

    this.review.reputing.reputed = 1;

    let contentRpt = this.postedRpt ?
        this.review.reputing.subtract(this.postedRpt) : this.review.reputing;

    this.logger.trace("onSave, ContentRpt:", contentRpt);
    this.backendService.acculateRepute(Content.name, this.content.idCid, contentRpt)
        .subscribe((rpt) => {
          this.content.reputation = rpt;
          this.postedRpt = Object.assign({}, this.review.reputing);
        });
  }

}
