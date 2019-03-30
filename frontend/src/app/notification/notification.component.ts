import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { filter } from 'rxjs/operators';
import { User, Notification, Category } from '../_models';
import { BackendService } from '../_services';
import { Config } from '../config';
import { NGXLogger } from 'ngx-logger';
import { userInfo } from 'os';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss']
})
export class NotificationComponent implements OnInit {

  @Input() isPreview: boolean = false;

  user: User;

  notifications : Notification[] = new Array<Notification>();

  nextInfo: string;

  constructor(
    private backendService: BackendService,
    private router: Router,
    private logger: NGXLogger) { }

  ngOnInit() {
    this.backendService.getUser().subscribe(user => this.user = user);
    this.getNext();
  }

  getNext(): Promise<Notification[]> {
    return this.backendService.getModels<Notification>(
      Notification, this.nextInfo, 'sid', this.user.identity,
      this.isPreview ? Config.paging.defaultPage : undefined,
      this.isPreview ? Config.paging.previewSize : undefined
    ).toPromise().then(respWithLink => {
      this.notifications = this.notifications.concat(respWithLink.response);
      this.nextInfo = respWithLink.getNextQueryParams();
      this.logger.trace("nextInfo:", this.nextInfo);
      return this.notifications;
    });
  }

  onDelete(index: number): Promise<Array<Notification>> {
    let noti = this.notifications[index];
    this.logger.trace('onDelete:', noti);
    return this.backendService.deleteModel(Notification, noti.idCid, this.user.identity)
        .toPromise()
        .then(_ => this.notifications.splice(index, 1));
  }

  onClick(index: number) {
    if (this.isPreview) {
      this.router.navigate(['/notification']);
    } else {
      let noti = this.notifications[index];
      this.logger.trace('onClick:', noti);
    }
  }

}
