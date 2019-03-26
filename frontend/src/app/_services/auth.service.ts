import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { tap, concatMap } from 'rxjs/operators';
import { User, Auth } from '../_models';
import { BackendService } from './backend.service';
import { Config } from '../config';
import { NGXLogger } from 'ngx-logger';

export function getAccessToken(): string {
  return localStorage.getItem('access_token');
}

export function setAccessToken(access_token?: string) {
  access_token ? localStorage.setItem('access_token', access_token) : localStorage.removeItem('access_token');
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  static AUTHENTICATION: string = 'authentication';

  auth: Auth;

  constructor(
    private backendService: BackendService,
    private logger: NGXLogger) {

    this.initAuth();
  }

  initAuth() {
    let strAuth = localStorage.getItem(AuthService.AUTHENTICATION);
    this.auth = strAuth ? JSON.parse(strAuth) : undefined;
    this.setAuthChangeUser(this.auth).subscribe(user => this.logger.trace("initAuth", user));
  }

  setAuthChangeUser(auth?: Auth): Observable<User> {
    this.logger.trace("setAuthChangeUser:", auth);
    if (auth) {
      this.auth = auth;
      setAccessToken(auth.access_token);
      localStorage.setItem(AuthService.AUTHENTICATION, JSON.stringify(auth));
      return this.backendService.changeUser(auth.username);
    } else {
      this.auth = undefined;
      setAccessToken();
      localStorage.removeItem(AuthService.AUTHENTICATION);
      return this.backendService.changeUser();
    }
  }

  signIn(username: string, password: string) {
    return this.backendService.authorize(username, password).pipe(
      tap(auth => auth.username = username),
      concatMap(auth => this.setAuthChangeUser(auth))
    );
  }

  signOut(): Observable<User> {
    return this.setAuthChangeUser();
  }

  isSignedIn(): boolean {
    return Boolean(this.auth);
  }

}

