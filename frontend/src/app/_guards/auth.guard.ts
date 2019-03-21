import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { NGXLogger } from 'ngx-logger';

import { BackendService } from '../_services';
import { Authority } from '../_models';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {

  constructor(
    private router: Router,
    private backendService: BackendService,
    private logger: NGXLogger) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {

    const user = this.backendService.getUserValue();

    if (route.data.a) {
      if (user.authorities.has(route.data.a)) {
        this.logger.trace("canActivate: true ", user, route);
        return true;
      } else {
        this.logger.trace("canActivate: false ", user, route);
        this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
        return false;
      }
    }
    this.logger.trace("canActivate: true ", user, route);
    return true;
  }
}