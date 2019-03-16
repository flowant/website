import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
import { filter, concatMap, tap, defaultIfEmpty } from 'rxjs/operators';
import { User, Notification } from '../protocols/model';
import { BackendService } from '../backend.service';
import { Config, Model } from '../config';
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

  imgServerUrl: string = Config.fileUrl + "/";

  constructor(
    private backendService: BackendService,
    private location: Location,
    private route: ActivatedRoute,
    private logger: NGXLogger) { }

  ngOnInit() {
    this.backendService.getUser("b901f010-4546-11e9-97e9-594de5a6cf90")
        .subscribe(user => {
          this.user = user;
          this.isPreview ? this.getPreview() : this.getNext();
        });
  }

  getPreview() {
    this.backendService.getModels<Notification>(Model.Notification, this.nextInfo,
        this.user.identity, 'sid', Config.defaultPage, Config.previewSize)
        .subscribe(respWithLink => {
          this.notifications = this.notifications.concat(respWithLink.response);
          this.nextInfo = respWithLink.getNextQueryParams();
        });
  }

  getNext() {
    this.backendService.getModels<Notification>(Model.Notification, this.nextInfo, this.user.identity, 'sid')
        .subscribe(respWithLink => {
          this.notifications = this.notifications.concat(respWithLink.response);
          this.nextInfo = respWithLink.getNextQueryParams();
          this.logger.trace("nextInfo:", this.nextInfo);
        });
  }

  onDelete(index: number) {
    let noti = this.notifications[index];
    this.logger.trace('onDelete:', noti);
    this.backendService.deleteModel<Notification>(Model.Notification, noti.idCid, this.user.identity)
      .subscribe(_ => this.notifications.splice(index, 1));
  }


}
