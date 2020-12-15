import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IUserQrCodeExposed, UserQrCodeExposed } from 'app/shared/model/user-qr-code-exposed.model';
import { UserQrCodeExposedService } from './user-qr-code-exposed.service';
import { UserQrCodeExposedComponent } from './user-qr-code-exposed.component';
import { UserQrCodeExposedDetailComponent } from './user-qr-code-exposed-detail.component';
import { UserQrCodeExposedUpdateComponent } from './user-qr-code-exposed-update.component';

@Injectable({ providedIn: 'root' })
export class UserQrCodeExposedResolve implements Resolve<IUserQrCodeExposed> {
  constructor(private service: UserQrCodeExposedService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserQrCodeExposed> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((userQrCodeExposed: HttpResponse<UserQrCodeExposed>) => {
          if (userQrCodeExposed.body) {
            return of(userQrCodeExposed.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UserQrCodeExposed());
  }
}

export const userQrCodeExposedRoute: Routes = [
  {
    path: '',
    component: UserQrCodeExposedComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bildwerkQrApp.userQrCodeExposed.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserQrCodeExposedDetailComponent,
    resolve: {
      userQrCodeExposed: UserQrCodeExposedResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bildwerkQrApp.userQrCodeExposed.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserQrCodeExposedUpdateComponent,
    resolve: {
      userQrCodeExposed: UserQrCodeExposedResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bildwerkQrApp.userQrCodeExposed.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserQrCodeExposedUpdateComponent,
    resolve: {
      userQrCodeExposed: UserQrCodeExposedResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bildwerkQrApp.userQrCodeExposed.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
