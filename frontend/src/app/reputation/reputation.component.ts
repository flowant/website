import { Component, OnInit, Input } from '@angular/core';
import { BackendService } from '../_services';
import { IdCid, Reputation, Content, User, Notification, Category } from '../_models';
import { NGXLogger } from 'ngx-logger';

const numeral = require('numeral');

@Component({
  selector: 'app-reputation',
  templateUrl: './reputation.component.html',
  styleUrls: ['./reputation.component.scss']
})
export class ReputationComponent implements OnInit {

  @Input() typeName: string;
  @Input() authorId: string;
  @Input() idCid: IdCid;
  @Input() reputation: Reputation;

  user: User;

  constructor(
    private backendService: BackendService,
    private logger: NGXLogger) { }

  ngOnInit() {
    this.backendService.getUser()
        .subscribe(user => this.user = user);
  }

  canRepute(): boolean {
    return !this.user.isGuest();
  }

  hasRated(): boolean {
    return Content.name === this.typeName;
  }

  isLiked(): boolean {
    return this.user.likes.has(this.idCid.identity);
  }

  toNumberal(num: number): string {
    return numeral(num).format('0a').toUpperCase();
  }

  onRepute(selected: string): Promise<Reputation> {
    let reputation = new Reputation();
    let liked = this.isLiked();
    let notification = Notification.of(this.user.identity, this.user.displayName,
        Category.Like, this.authorId, this.idCid.identity, this.idCid.containerId, this.typeName);

    if (liked === false) {
      this.user.likes.add(this.idCid.identity);
      this.backendService.postModel<Notification>(Notification, notification)
          .toPromise();
    } else {
      this.user.likes.delete(this.idCid.identity);
    }

    // TODO handle error
    this.backendService.postUser(this.user)
        .toPromise();

    reputation.select(selected, !liked);
    reputation.idCid = this.idCid;

    return this.backendService.acculateRepute(this.typeName, this.idCid, reputation)
        .toPromise()
        .then(rpt => this.reputation = rpt);
  }

}
