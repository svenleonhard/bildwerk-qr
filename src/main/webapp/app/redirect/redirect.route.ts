import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { RedirectComponent } from './redirect.component';

export const REDIRECT_ROUTE: Route = {
  path: 'redirect',
  component: RedirectComponent,
  data: {
    authorities: [],
    pageTitle: 'redirect.title',
  },
  canActivate: [UserRouteAccessService],
};
