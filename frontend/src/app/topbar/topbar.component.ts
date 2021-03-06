import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NGXLogger } from 'ngx-logger';
import { User } from '../_models';
import { BackendService } from '../_services';
import { Config } from '../config';

@Component({
  selector: 'app-topbar',
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.scss']
})
export class TopbarComponent implements OnInit {

  searchTag: string;

  user: User;

  userImgUrl: string;

  constructor(
    private backendService: BackendService,
    private router: Router,
    private logger: NGXLogger) { }

  ngOnInit() {
    this.backendService.getUser().subscribe(user => {
      this.user = user;
      this.userImgUrl = Config.imgServerUrl + '/' + user.identity;
    });
  }

  onSearch() {
    if (this.searchTag) {
      this.logger.trace("onSearch(), searchTag", this.searchTag);
      this.router.navigate(['/', 'search', this.searchTag]);
    }
  }

}
