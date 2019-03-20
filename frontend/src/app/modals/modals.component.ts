import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, of } from 'rxjs';
import { filter, concatMap, tap, defaultIfEmpty } from 'rxjs/operators';
import { User, Notification, Category } from '../_models';
import { BackendService } from '../_services';
import { Config, Model } from '../config';
import { NGXLogger, LoggerConfig } from 'ngx-logger';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-modals',
  templateUrl: './modals.component.html',
  styleUrls: ['./modals.component.scss']
})
export class ModalsComponent implements OnInit {

  constructor(private backendService: BackendService,
    private location: Location,
    private route: ActivatedRoute,
    private logger: NGXLogger) { }

  ngOnInit() {
  }

  sendMessage() {
    this.logger.trace("sendMessage");
  }

}
