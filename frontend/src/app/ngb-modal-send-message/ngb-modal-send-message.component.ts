import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { map, concatMap } from 'rxjs/operators';
import { Message } from '../_models';
import { BackendService } from '../_services';
import { Model } from '../config';
import { NGXLogger } from 'ngx-logger';

@Component({
  selector: 'app-ngb-modal-send-message',
  templateUrl: './ngb-modal-send-message.component.html',
  styleUrls: ['./ngb-modal-send-message.component.scss']
})
export class NgbModalSendMessageComponent implements OnInit {

  @Input() receiverId: string;
  @Input() receiverName: string;
  sentences: string;

  constructor(
    public activeModal: NgbActiveModal,
    private backendService: BackendService,
    private logger: NGXLogger) { }

  ngOnInit() {
  }

  send() {
    this.backendService.getUser().pipe(
        map(user => Message.of(this.receiverId, this.receiverName, user.identity, user.displayName, this.sentences)),
        concatMap(msg => this.backendService.postModel<Message>(Model.Message, msg))
    ).subscribe(m => {
      this.logger.trace("The message has been sent:", m);
    });

    this.activeModal.close("The message is sent.");
  }

}
