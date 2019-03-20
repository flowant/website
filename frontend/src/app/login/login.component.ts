import { Component, OnInit } from '@angular/core';
import { AuthService } from '../_services';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.authService.login('user0', 'pass0').subscribe();
  }

}
