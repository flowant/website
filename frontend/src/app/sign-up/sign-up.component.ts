import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { first } from 'rxjs/operators';

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
    if (this.authService.auth) {
      this.router.navigate(['/']);
    }
  }

  ngOnInit() {
    this.signUpForm = this.formBuilder.group({
      username: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', Validators.required],
      confirm: ['', Validators.required]
    }, { validator: this.validate });

    this.returnUrl = '/';
  }

  validate(group: FormGroup) {
    return;//TODO remove
    // TODO make a policy and refine

    // validate username
    if (group.controls.username.value.length < 8) {
      group.controls.username.setErrors({ tooShort: true });
    }

    // validate email
    if (group.controls.email.value.includes('@') === false) {
      group.controls.email.setErrors({ mustContain: true });
    }

    // validate password
    if (group.controls.password.value.length < 8) {
      group.controls.password.setErrors({ tooShort: true });
    }

    // TODO refine
    if (group.controls.password.value.includes('{')) {
      group.controls.password.setErrors({ invalidCharacter: true });
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

    this.backendService.signUpUser(newUser)
        .subscribe(
          _ => {
            // this.router.navigate(['/']);
          },
          error => {
            this.error = error;
            this.loading = false;
            this.logger.trace('signup error:', error);
          }
        );
  }

}
