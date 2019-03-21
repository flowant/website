import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BackendService, AuthService } from '../_services';
import { NGXLogger } from 'ngx-logger';

@Component({
  selector: 'app-modals',
  templateUrl: './modals.component.html',
  styleUrls: ['./modals.component.scss']
})
export class ModalsComponent implements OnInit {

  constructor(private backendService: BackendService,
    private authService: AuthService,
    private router: Router,
    private logger: NGXLogger) { }

  ngOnInit() {
  }

  logout() {
    this.authService.logout()
        .subscribe(_ => this.router.navigate(['/']));
  }

}
