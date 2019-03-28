import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';
import { concatMap } from 'rxjs/operators';

import { AuthService, BackendService } from '../_services';
import { NGXLogger } from 'ngx-logger';
import { User } from '../_models';
import { userInfo } from 'os';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.scss']
})
export class SignUpComponent implements OnInit {

  signUpForm: FormGroup;
  pending = false;

  returnUrl: string;
  error = '';

  constructor(
    private formBuilder: FormBuilder,
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
      username: ['', [
        Validators.required,
        Validators.minLength(6)
      ]],
      email: ['', [
        Validators.required,
        Validators.email
      ]],
      password: ['', [
        Validators.required,
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{8,})/),
        Validators.pattern(/^[^{}]+$/) // cannot contain { or } character.
      ]],
      confirm: [''],
      accept: [false, Validators.requiredTrue]
    }, { validator: this.passwordConfirmValidator });

    this.returnUrl = '/';
  }

  passwordConfirmValidator: ValidatorFn = (group: FormGroup): ValidationErrors | null => {
    return group.controls.password.value !== group.controls.confirm.value ? { notSame: true } : null;
  }

  get f() {
    return this.signUpForm.controls;
  }

  isInvalid(control): boolean {
    return control.invalid && (control.dirty || control.touched);
  }

  onSubmit(): Promise<User> {
    // this.submitted = true;

    if (this.signUpForm.invalid) {
      return;
    }

    this.pending = true;

    let newUser: User = User.of(this.f.username.value, this.f.email.value, this.f.password.value);

    this.logger.trace("signUpUser:", newUser);

    return this.backendService.signUpUser(newUser).pipe(
      concatMap(u =>  this.authService.signIn(this.f.username.value, this.f.password.value))
    ).toPromise().then(
      (user: User) => {
        this.pending = false;
        this.router.navigate(['/user/profile']);
        return user;
      },
      error => {
        this.pending = false;
        this.error = error;
        return error;
      }
    );
  }

}
