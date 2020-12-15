import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BildwerkQrSharedModule } from 'app/shared/shared.module';
import { UserQrCodeExposedComponent } from './user-qr-code-exposed.component';
import { UserQrCodeExposedDetailComponent } from './user-qr-code-exposed-detail.component';
import { UserQrCodeExposedUpdateComponent } from './user-qr-code-exposed-update.component';
import { UserQrCodeExposedDeleteDialogComponent } from './user-qr-code-exposed-delete-dialog.component';
import { userQrCodeExposedRoute } from './user-qr-code-exposed.route';

@NgModule({
  imports: [BildwerkQrSharedModule, RouterModule.forChild(userQrCodeExposedRoute)],
  declarations: [
    UserQrCodeExposedComponent,
    UserQrCodeExposedDetailComponent,
    UserQrCodeExposedUpdateComponent,
    UserQrCodeExposedDeleteDialogComponent,
  ],
  entryComponents: [UserQrCodeExposedDeleteDialogComponent],
})
export class BildwerkQrUserQrCodeExposedModule {}
