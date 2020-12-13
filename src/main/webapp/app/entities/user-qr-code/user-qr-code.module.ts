import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BildwerkQrSharedModule } from 'app/shared/shared.module';
import { UserQrCodeComponent } from './user-qr-code.component';
import { UserQrCodeDetailComponent } from './user-qr-code-detail.component';
import { UserQrCodeUpdateComponent } from './user-qr-code-update.component';
import { UserQrCodeDeleteDialogComponent } from './user-qr-code-delete-dialog.component';
import { userQrCodeRoute } from './user-qr-code.route';

@NgModule({
  imports: [BildwerkQrSharedModule, RouterModule.forChild(userQrCodeRoute)],
  declarations: [UserQrCodeComponent, UserQrCodeDetailComponent, UserQrCodeUpdateComponent, UserQrCodeDeleteDialogComponent],
  entryComponents: [UserQrCodeDeleteDialogComponent],
})
export class BildwerkQrUserQrCodeModule {}
