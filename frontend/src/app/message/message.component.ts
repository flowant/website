import { Component, OnInit, Input } from '@angular/core';
import { User, Message } from '../_models';
import { BackendService, IdParams } from '../_services';
import { Config } from '../config';
import { NGXLogger } from 'ngx-logger';
import { Router } from '@angular/router';

export enum Option {
  Sent,
  Received,
  Preview
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
  preview = Option.Preview;

  msgMap: Map<Option, Message[]> = new Map();
  paramsMap: Map<Option, IdParams> = new Map();
  nextInfoMap: Map<Option, string> = new Map();

  constructor(
    private backendService: BackendService,
    private router: Router,
    private logger: NGXLogger) { }

  ngOnInit() {
    this.msgMap.set(Option.Received, new Array<Message>())
    this.msgMap.set(Option.Sent, new Array<Message>())

    this.backendService.getUser().subscribe(user => {
      this.user = user;
      this.paramsMap.set(Option.Received, {cid: user.identity});
      this.paramsMap.set(Option.Sent, {aid: user.identity});
    });

    if (this.isPreview) {
      this.getPreview();
    } else {
      this.getNext(Option.Received);
      this.getNext(Option.Sent);
    }
  }

  getPreview(): Promise<Message[]> {
    return this.backendService.getModels<Message>(Message, this.nextInfoMap.get(Option.Received),
        this.paramsMap.get(Option.Received), Config.paging.defaultPage, Config.paging.previewSize)
        .toPromise()
        .then(respWithLink => {
          this.msgMap.set(Option.Received, this.msgMap.get(Option.Received).concat(respWithLink.response));
          this.nextInfoMap.set(Option.Received, respWithLink.getNextQueryParams());
          return this.msgMap.get(Option.Received);
        });
  }

  getNext(option: Option): Promise<Message[]> {
    return this.backendService.getModels<Message>(Message, this.nextInfoMap.get(option),
        this.paramsMap.get(option))
        .toPromise()
        .then(respWithLink => {
          this.msgMap.set(option, this.msgMap.get(option).concat(respWithLink.response));
          this.nextInfoMap.set(option, respWithLink.getNextQueryParams());
          this.logger.trace("nextInfo:", this.nextInfoMap.get(option));
          return this.msgMap.get(option);
        });
  }

  onDelete(option: Option, index: number): Promise<Message[]> {
    let message = this.msgMap.get(option)[index];
    this.logger.trace('onDelete:', message);
    return this.backendService.deleteModel(Message, message.idCid)
        .toPromise()
        .then(_ => this.msgMap.get(option).splice(index, 1));
  }

  onClick(option: Option, index: number) {
    if (option === Option.Preview) {
      this.router.navigate(['/message']);
    } else {
      let message = this.msgMap.get(option)[index];
      this.logger.trace('onClick:', message);
    }
  }

}
