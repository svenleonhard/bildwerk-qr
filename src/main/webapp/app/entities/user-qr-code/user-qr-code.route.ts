import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IUserQrCode, UserQrCode } from 'app/shared/model/user-qr-code.model';
import { UserQrCodeService } from './user-qr-code.service';
import { UserQrCodeComponent } from './user-qr-code.component';
import { UserQrCodeDetailComponent } from './user-qr-code-detail.component';
import { UserQrCodeUpdateComponent } from './user-qr-code-update.component';

@Injectable({ providedIn: 'root' })
export class UserQrCodeResolve implements Resolve<IUserQrCode> {
  constructor(private service: UserQrCodeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserQrCode> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((userQrCode: HttpResponse<UserQrCode>) => {
          if (userQrCode.body) {
            return of(userQrCode.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UserQrCode());
  }
}

export const userQrCodeRoute: Routes = [
  {
    path: '',
    component: UserQrCodeComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bildwerkQrApp.userQrCode.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserQrCodeDetailComponent,
    resolve: {
      userQrCode: UserQrCodeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bildwerkQrApp.userQrCode.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserQrCodeUpdateComponent,
    resolve: {
      userQrCode: UserQrCodeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bildwerkQrApp.userQrCode.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserQrCodeUpdateComponent,
    resolve: {
      userQrCode: UserQrCodeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bildwerkQrApp.userQrCode.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
