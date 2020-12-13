import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BildwerkQrSharedModule } from 'app/shared/shared.module';
import { QrRouteComponent } from './qr-route.component';
import { QrRouteDetailComponent } from './qr-route-detail.component';
import { QrRouteUpdateComponent } from './qr-route-update.component';
import { QrRouteDeleteDialogComponent } from './qr-route-delete-dialog.component';
import { qrRouteRoute } from './qr-route.route';

@NgModule({
  imports: [BildwerkQrSharedModule, RouterModule.forChild(qrRouteRoute)],
  declarations: [QrRouteComponent, QrRouteDetailComponent, QrRouteUpdateComponent, QrRouteDeleteDialogComponent],
  entryComponents: [QrRouteDeleteDialogComponent],
})
export class BildwerkQrQrRouteModule {}
