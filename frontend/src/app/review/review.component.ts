import { Component, OnInit, Input } from '@angular/core';
import { IdCid, Content, Review, Reputation, User, Category, Notification } from '../_models';
import { BackendService } from '../_services'
import { NGXLogger } from 'ngx-logger';
import { tap, filter, concatMap, defaultIfEmpty, map } from 'rxjs/operators';
import { userInfo } from 'os';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.scss']
})
export class ReviewComponent implements OnInit {

  @Input() content: Content;

  user: User;

  userReview: Review;

  reviews: Review[] = new Array<Review>();

  nextInfo: string;

  postedRpt: Reputation;

  constructor(
    private backendService: BackendService,
    private logger: NGXLogger) { }

  ngOnInit() {

    if (Boolean(this.content) == false) {
      this.logger.error('Review, content is invalid');
      return;
    }

    this.backendService.getUser().subscribe(user => {
      this.user = user;
      if (!user.isGuest()) {
        this.backendService.getModels<Review>(Review, undefined,
            { aid: user.identity, cid: this.content.idCid.identity })
            .pipe(
              map(respWithLink => respWithLink.response[0]),
              filter(Boolean),
              defaultIfEmpty(Review.of(this.content.idCid.identity, this.user))
            ).toPromise().then(review => {
              this.setUserReview(review);
              this.logger.trace("userReview:", this.userReview, this.postedRpt);
            });
      }
    });

    this.getPopular();
  }

  setUserReview(review: Review) {
    this.userReview = review;
    this.postedRpt = Object.assign(new Reputation(), review.reputing);
  }

  getPopular(): void {
    this.backendService.getPopularItems<Review>(Review, this.content.idCid.identity)
        .toPromise().then(pupolarReviews => {
          this.reviews = this.reviews.concat(pupolarReviews);
          this.getNext();
        });
  }

  getNext() {
    this.backendService.getModels<Review>(Review, this.nextInfo, {cid: this.content.idCid.identity})
        .toPromise().then(respWithLink => {
          this.reviews = this.reviews.concat(respWithLink.response);
          this.nextInfo = respWithLink.getNextQueryParams();
          this.logger.trace("nextInfo:", this.nextInfo);
        });
  }

  onSave(): void {
    this.userReview.reputing.adjust();
    this.logger.trace('onSave:', this.userReview);
    this.backendService.postModel<Review>(Review, this.userReview)
        .toPromise().then();

    let contentRpt = this.userReview.reputing.subtract(this.postedRpt);

    this.logger.trace("onSave, ContentRpt:", contentRpt);
    this.backendService.acculateRepute(Content.name, this.content.idCid, contentRpt)
        .toPromise().then((rpt) => {
          this.content.reputation = rpt;
          this.postedRpt = Object.assign(new Reputation(), this.userReview.reputing);
        });

    let notification = Notification.of(this.user.identity, this.user.displayName,
        Category.NewReview, this.content.authorId, this.userReview.idCid.identity, this.userReview.idCid.containerId, Review.name);
    this.backendService.postModel<Notification>(Notification, notification)
        .toPromise();
  }

  onDelete(): void {
    this.logger.trace('onDelete:', this.userReview);

    this.backendService.acculateRepute(Content.name, this.content.idCid, this.postedRpt.negative())
        .toPromise().then((rpt) => {
          this.content.reputation = rpt;
        });

    this.backendService.deleteModel(Review, this.userReview.idCid)
        .toPromise().then(_ => this.setUserReview(Review.of(this.content.idCid.identity, this.user)));

  }

}
