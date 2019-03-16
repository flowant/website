import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NGXLogger } from 'ngx-logger';
import { User } from '../protocols/model';
import { BackendService } from '../backend.service';
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

  userImage: string;

  imgServerUrl: string = Config.imgServerUrl + '/';

  constructor(
    private backendService: BackendService,
    private router: Router,
    private logger: NGXLogger) { }

  ngOnInit() {
    this.backendService.getModel<User>(Model.User, "b901f010-4546-11e9-97e9-594de5a6cf90")
          .subscribe(u => {
            this.user = u;
            this.userImage = Config.imgServerUrl + '/' + this.user.identity;
          });
  }

  onSearch() {
    this.logger.trace("onSearch(), searchTag", this.searchTag);
    if (this.searchTag) {
      this.router.navigate(['/', 'search', this.searchTag]);
    }
  }

}
