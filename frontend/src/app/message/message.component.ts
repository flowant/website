import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
import { filter, concatMap, tap, defaultIfEmpty } from 'rxjs/operators';
import { User, Message } from '../protocols/model';
import { BackendService } from '../backend.service';
import { Config, Model } from '../config';
import { NGXLogger } from 'ngx-logger';

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss']
})
export class MessageComponent implements OnInit {

  @Input() isPreview: boolean = false;

  user: User;

  messages : Message[] = new Array<Message>();

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
    this.backendService.getModels<Message>(Model.Message, this.nextInfo,
        this.user.identity, 'cid', Config.defaultPage, Config.previewSize)
        .subscribe(respWithLink => {
          this.messages = this.messages.concat(respWithLink.response);
          this.nextInfo = respWithLink.getNextQueryParams();
        });
  }

  getNext() {
    this.backendService.getModels<Message>(Model.Message, this.nextInfo, this.user.identity)
        .subscribe(respWithLink => {
          this.messages = this.messages.concat(respWithLink.response);
          this.nextInfo = respWithLink.getNextQueryParams();
          this.logger.trace("nextInfo:", this.nextInfo);
        });
  }

  onClick(index: number) {
    let message = this.messages[index];
    this.logger.trace('onClick:', message);
  }

  onDelete(index: number) {
    let message = this.messages[index];
    this.logger.trace('onDelete:', message);
    this.backendService.deleteModel<Message>(Model.Message, message.idCid)
      .subscribe(_ => this.messages.splice(index, 1));
  }

}
