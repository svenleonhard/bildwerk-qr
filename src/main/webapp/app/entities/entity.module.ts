import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'qr-route',
        loadChildren: () => import('./qr-route/qr-route.module').then(m => m.BildwerkQrQrRouteModule),
      },
      {
        path: 'user-qr-code',
        loadChildren: () => import('./user-qr-code/user-qr-code.module').then(m => m.BildwerkQrUserQrCodeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class BildwerkQrEntityModule {}
