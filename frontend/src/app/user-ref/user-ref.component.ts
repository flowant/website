import { Component, OnInit, Input } from '@angular/core';
import { concatMap } from 'rxjs/operators';
import { BackendService } from '../_services';
import { Config } from '../config';
import { NGXLogger } from 'ngx-logger';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgbModalSendMessageComponent } from '../ngb-modal-send-message/ngb-modal-send-message.component';
import { User, Relation } from '../_models';



@Component({
  selector: 'app-user-ref',
  templateUrl: './user-ref.component.html',
  styleUrls: ['./user-ref.component.scss']
})
export class UserRefComponent implements OnInit {

  @Input() userRefId: string;
  @Input() userRefName: string;

  imgPath: string = Config.imgServerUrl + "/";

  relation: Relation;

  user: User;

  constructor(
    private backendService: BackendService,
    private modalService: NgbModal,
    private logger: NGXLogger) { }

  ngOnInit() {

    this.backendService.getRelation()
        .subscribe(relation => this.relation = relation);

    this.backendService.getUser()
        .subscribe(user => this.user = user);

  }

  hasFollowee(): boolean {
    return this.relation.hasFollowee(this.userRefId);
  }

  canShowMenu(): boolean {
    return !this.user.isGuest() && !this.user.isMe(this.userRefId);
  }

  postRelation(follow: boolean): Promise<Relation> {
    return this.backendService.postRelation(follow, this.user.identity, this.userRefId)
        .toPromise()
        .then();
  }

  sendMessage() {
    const modalRef = this.modalService.open(NgbModalSendMessageComponent);
    modalRef.componentInstance.receiverId = this.userRefId;
    modalRef.componentInstance.receiverName = this.userRefName;
  }

}
