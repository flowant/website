import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../_services';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.authService && this.authService.auth && this.authService.auth.access_token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.authService.auth.access_token}`
        }
      });
    }

    return next.handle(request);
  }
}