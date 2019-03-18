import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
import { filter, concatMap, tap, defaultIfEmpty } from 'rxjs/operators';
import { User, Notification, Category } from '../protocols/model';
import { BackendService } from '../backend.service';
import { Config, Model } from '../config';
import { NGXLogger, LoggerConfig } from 'ngx-logger';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgbModalSendMessageComponent } from '../ngb-modal-send-message/ngb-modal-send-message.component';



@Component({
  selector: 'app-user-ref',
  templateUrl: './user-ref.component.html',
  styleUrls: ['./user-ref.component.scss']
})
export class UserRefComponent implements OnInit {

  @Input() user: User;
  @Input() userRefId: string;
  @Input() userRefName: string;

  imgServerUrl: string = Config.fileUrl + "/";

  constructor(
    private backendService: BackendService,
    private modalService: NgbModal,
    private location: Location,
    private route: ActivatedRoute,
    private logger: NGXLogger) { }

  ngOnInit() {
  }

  sendMessage() {
    const modalRef = this.modalService.open(NgbModalSendMessageComponent);
    modalRef.componentInstance.receiverId = this.userRefId;
    modalRef.componentInstance.receiverName = this.userRefName;
  }

  follow(isFollowing: boolean) {
    this.backendService.follow(isFollowing, this.user.identity, this.userRefId)
        .subscribe();
  }

}
