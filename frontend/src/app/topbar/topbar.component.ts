import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NGXLogger } from 'ngx-logger';
import { User } from '../_models';
import { BackendService } from '../_services';
import { Config, Model } from '../config';
import { userInfo } from 'os';

@Component({
  selector: 'app-topbar',
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.scss']
})
export class TopbarComponent implements OnInit {

  searchTag: string;

  user: User;

  userImgUrl: string;

  isGuest = User.isGuest;

  constructor(
    private backendService: BackendService,
    private router: Router,
    private logger: NGXLogger) { }

  ngOnInit() {
    this.backendService.getUser()
          .subscribe(u => {
            this.user = u;
            this.userImgUrl = Config.imgServerUrl + '/' + this.user.identity;
          });
  }

  onSearch() {
    this.logger.trace("onSearch(), searchTag", this.searchTag);
    if (this.searchTag) {
      this.router.navigate(['/', 'search', this.searchTag]);
    }
  }

}
