import { Component, OnInit } from '@angular/core';
import { Notification } from '../protocols/model';
import { BackendService } from '../backend.service';
import { Config, Model } from '../config';
import { NGXLogger, LoggerConfig } from 'ngx-logger';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss']
})
export class NotificationComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
