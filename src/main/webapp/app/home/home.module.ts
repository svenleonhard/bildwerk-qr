import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BildwerkQrSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { QrDataDisplayModule } from 'app/qr-data-display/qr-data-display.module';

@NgModule({
  imports: [BildwerkQrSharedModule, RouterModule.forChild([HOME_ROUTE]), QrDataDisplayModule],
  declarations: [HomeComponent],
})
export class BildwerkQrHomeModule {}
