import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BildwerkQrSharedModule } from '../shared/shared.module';

import { REDIRECT_ROUTE, RedirectComponent } from './';

@NgModule({
  imports: [BildwerkQrSharedModule, RouterModule.forRoot([REDIRECT_ROUTE], { useHash: true })],
  declarations: [RedirectComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BildwerkQrAppRedirectModule {}
