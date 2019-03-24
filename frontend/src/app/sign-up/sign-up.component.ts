import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { concatMap } from 'rxjs/operators';

import { AuthService, BackendService } from '../_services';
import { NGXLogger } from 'ngx-logger';
import { User } from '../_models';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.scss']
})
export class SignUpComponent implements OnInit {

  signUpForm: FormGroup;
  loading = false;
  submitted = false;
  returnUrl: string;
  error = '';

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private backendService: BackendService,
    private logger: NGXLogger) {

    // redirect to home if already logged in
    if (this.authService.isSignedIn()) {
      this.router.navigate(['/']);
    }
  }

  ngOnInit() {
    this.signUpForm = this.formBuilder.group({
      username: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', Validators.required],
      confirm: ['', Validators.required],
      accept: [false, Validators.required]
    }, { validator: this.validate });

    this.returnUrl = '/';
  }

  validate(group: FormGroup) {

    let mustNotContain = new RegExp("^(?=.*[{}])");

    // validate username
    if (group.controls.username.value.length < 6) {
      group.controls.username.setErrors({ tooShort: true });
    }

    if (mustNotContain.test(group.controls.username.value) === true) {
      group.controls.username.setErrors({ mustNotContain: true });
    }

    // validate email
    if (group.controls.email.value.length < 5) {
      group.controls.email.setErrors({ tooShort: true });
    }

    if (group.controls.email.value.includes('@') === false) {
      group.controls.email.setErrors({ mustContain: true });
    }

    // validate password
    let passwordMustContain = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{8,})");
    if (passwordMustContain.test(group.controls.password.value) === false) {
      group.controls.password.setErrors({ mustContain: true });
    }

    if (mustNotContain.test(group.controls.password.value) === true) {
      group.controls.password.setErrors({ mustNotContain: true });
    }

    // validate password and confirmation
    if (group.controls.password.value !== group.controls.confirm.value) {
      group.controls.confirm.setErrors({ notSame: true });
    }
  }

  get f() { 
    return this.signUpForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    if (this.signUpForm.invalid) {
      return;
    }

    this.loading = true;

    let newUser: User = User.of(this.f.username.value, this.f.email.value, this.f.password.value);

    this.logger.trace("signUpUser:", newUser);

    this.backendService.signUpUser(newUser).pipe(
      concatMap(u =>  this.authService.signIn(this.f.username.value, this.f.password.value))
    ).subscribe(
      _ => {
        this.loading = false;
        this.router.navigate(['/user/profile']);
      },
      error => {
        this.loading = false;
        this.error = error;
      }
    );
  }

}
