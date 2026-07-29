import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { ReaExpressService } from './rea-express.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {
  constructor(private reaService: ReaExpressService, private router: Router) {}

  canActivate(): boolean | UrlTree {
    if (this.reaService.isLoggedIn() && this.reaService.isAdmin()) {
      return true;
    }
    if (this.reaService.isLoggedIn()) {
      return this.router.createUrlTree(['/espace-client']);
    }
    return this.router.createUrlTree(['/login']);
  }
}
