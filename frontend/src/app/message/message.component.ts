import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
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

  user: User;

  messages : Message[] = new Array<Message>();

  nextInfo: string;

  imgServerUrl: string = Config.gatewayUrl;

  constructor(
    private backendService: BackendService,
    private location: Location,
    private route: ActivatedRoute,
    private logger: NGXLogger) { }

  ngOnInit() {
    this.backendService.getUser("b901f010-4546-11e9-97e9-594de5a6cf90").pipe(

    )

    .subscribe(u => this.user = u);
  }

  getNextMessages() {
    this.backendService.getModels<Message>(Model.Message, this.nextInfo, this.user.identity)
        .subscribe(respWithLink => {
          this.messages = this.messages.concat(respWithLink.response);
          this.nextInfo = respWithLink.getNextQueryParams();
          this.logger.trace("nextInfo:", this.nextInfo);
        });
  }

}
