import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
import { filter, concatMap, tap, defaultIfEmpty } from 'rxjs/operators';
import { User, Notification, Category } from '../_models';
import { BackendService } from '../_services';
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

  @Input() userRefId: string;
  @Input() userRefName: string;

  imgServerUrl: string = Config.fileUrl + "/";

  isFollowee: boolean;

  constructor(
    private backendService: BackendService,
    private modalService: NgbModal,
    private location: Location,
    private route: ActivatedRoute,
    private logger: NGXLogger) { }

  ngOnInit() {
    this.updateRelation();
  }

  updateRelation() {
    this.backendService.getRelation().subscribe(relation => {
      this.isFollowee = relation.followings.has(this.userRefId);
    });
  }

  postRelation(follow: boolean) {
    this.backendService.getUser()
        .pipe(concatMap(user => this.backendService.postRelation(follow, user.identity, this.userRefId)))
        .subscribe(_ => this.updateRelation());
  }

  sendMessage() {
    const modalRef = this.modalService.open(NgbModalSendMessageComponent);
    modalRef.componentInstance.receiverId = this.userRefId;
    modalRef.componentInstance.receiverName = this.userRefName;
  }

}
