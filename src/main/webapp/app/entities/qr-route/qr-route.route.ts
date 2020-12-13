import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IQrRoute, QrRoute } from 'app/shared/model/qr-route.model';
import { QrRouteService } from './qr-route.service';
import { QrRouteComponent } from './qr-route.component';
import { QrRouteDetailComponent } from './qr-route-detail.component';
import { QrRouteUpdateComponent } from './qr-route-update.component';

@Injectable({ providedIn: 'root' })
export class QrRouteResolve implements Resolve<IQrRoute> {
  constructor(private service: QrRouteService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQrRoute> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((qrRoute: HttpResponse<QrRoute>) => {
          if (qrRoute.body) {
            return of(qrRoute.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new QrRoute());
  }
}

export const qrRouteRoute: Routes = [
  {
    path: '',
    component: QrRouteComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bildwerkQrApp.qrRoute.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QrRouteDetailComponent,
    resolve: {
      qrRoute: QrRouteResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bildwerkQrApp.qrRoute.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QrRouteUpdateComponent,
    resolve: {
      qrRoute: QrRouteResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bildwerkQrApp.qrRoute.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QrRouteUpdateComponent,
    resolve: {
      qrRoute: QrRouteResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bildwerkQrApp.qrRoute.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
