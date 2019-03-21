import { Component, OnInit, Input } from '@angular/core';
import { User, Message } from '../_models';
import { BackendService } from '../_services';
import { Config, Model } from '../config';
import { NGXLogger, LoggerConfig } from 'ngx-logger';

export enum Option {
  Sent,
  Received
}

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss']
})
export class MessageComponent implements OnInit {

  @Input() isPreview: boolean = false;

  user: User;

  sent = Option.Sent;
  received = Option.Received;

  msgMap: Map<Option, Message[]> = new Map();
  paramNameMap: Map<Option, string> = new Map();
  nextInfoMap: Map<Option, string> = new Map();

  imgServerUrl: string = Config.fileUrl + "/";

  constructor(
    private backendService: BackendService,
    private logger: NGXLogger) { }

  ngOnInit() {
    this.msgMap.set(Option.Received, new Array<Message>())
    this.msgMap.set(Option.Sent, new Array<Message>())
    this.paramNameMap.set(Option.Received, 'cid');
    this.paramNameMap.set(Option.Sent, 'aid');

    this.backendService.getUser()
        .subscribe(user => {
          this.logger.trace("getUser Subscription:", user);
          this.user = user;
          if (this.isPreview) {
            this.getPreview();
          } else {
            this.getNext(Option.Received);
            this.getNext(Option.Sent);
          }
        });
  }

  getPreview() {
    this.backendService.getModels<Message>(Model.Message, this.nextInfoMap.get(Option.Received),
        this.paramNameMap.get(Option.Received), this.user.identity, Config.defaultPage, Config.previewSize)
        .subscribe(respWithLink => {
          this.msgMap.set(Option.Received, this.msgMap.get(Option.Received).concat(respWithLink.response));
          this.nextInfoMap.set(Option.Received, respWithLink.getNextQueryParams());
        });
  }

  getNext(option: Option) {
    this.backendService.getModels<Message>(Model.Message, this.nextInfoMap.get(option),
        this.paramNameMap.get(option), this.user.identity)
        .subscribe(respWithLink => {
          this.msgMap.set(option, this.msgMap.get(option).concat(respWithLink.response));
          this.nextInfoMap.set(option, respWithLink.getNextQueryParams());
          this.logger.trace("nextInfo:", this.nextInfoMap.get(option));
        });
  }

  onClick(option: Option, index: number) {
    this.logger.trace('onClick, option:', option);
    let message = this.msgMap.get(option)[index];
    this.logger.trace('onClick:', message);
  }

  onDelete(option: Option, index: number) {
    let message = this.msgMap.get(option)[index];
    this.logger.trace('onDelete:', message);
    this.backendService.deleteModel<Message>(Model.Message, message.idCid)
        .subscribe(_ => this.msgMap.get(option).splice(index, 1));
  }

}
