import { Component, OnInit, Input } from '@angular/core';
import { filter } from 'rxjs/operators';
import { User, Message } from '../_models';
import { BackendService } from '../_services';
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
  paramNameMap: Map<Option, string> = new Map();
  nextInfoMap: Map<Option, string> = new Map();

  imgServerUrl: string = Config.fileUrl + "/";

  constructor(
    private backendService: BackendService,
    private router: Router,
    private logger: NGXLogger) { }

  ngOnInit() {
    this.msgMap.set(Option.Received, new Array<Message>())
    this.msgMap.set(Option.Sent, new Array<Message>())
    this.paramNameMap.set(Option.Received, 'cid');
    this.paramNameMap.set(Option.Sent, 'aid');

    this.backendService.getUser().subscribe(user => this.user = user);

    if (this.isPreview) {
      this.getPreview();
    } else {
      this.getNext(Option.Received);
      this.getNext(Option.Sent);
    }
  }

  getPreview() {
    this.backendService.getModels<Message>(Message, this.nextInfoMap.get(Option.Received),
        this.paramNameMap.get(Option.Received), this.user.identity, Config.paging.defaultPage, Config.paging.previewSize)
        .toPromise().then(respWithLink => {
          this.msgMap.set(Option.Received, this.msgMap.get(Option.Received).concat(respWithLink.response));
          this.nextInfoMap.set(Option.Received, respWithLink.getNextQueryParams());
        });
  }

  getNext(option: Option) {
    this.backendService.getModels<Message>(
      Message,
      this.nextInfoMap.get(option),
      this.paramNameMap.get(option),
      this.user.identity
    ).toPromise().then(respWithLink => {
      this.msgMap.set(option, this.msgMap.get(option).concat(respWithLink.response));
      this.nextInfoMap.set(option, respWithLink.getNextQueryParams());
      this.logger.trace("nextInfo:", this.nextInfoMap.get(option));
    });
  }

  onClick(option: Option, index: number) {
    if (option === Option.Preview) {
      this.router.navigate(['/message']);
    } else {
      let message = this.msgMap.get(option)[index];
      this.logger.trace('onClick:', message);
    }
  }

  onDelete(option: Option, index: number) {
    let message = this.msgMap.get(option)[index];
    this.logger.trace('onDelete:', message);
    this.backendService.deleteModel(Message, message.idCid)
        .toPromise()
        .then(_ => this.msgMap.get(option).splice(index, 1));
  }

}
