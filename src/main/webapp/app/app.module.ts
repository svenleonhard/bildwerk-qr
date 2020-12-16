import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { BildwerkQrSharedModule } from 'app/shared/shared.module';
import { BildwerkQrCoreModule } from 'app/core/core.module';
import { BildwerkQrAppRoutingModule } from './app-routing.module';
import { BildwerkQrHomeModule } from './home/home.module';
import { BildwerkQrEntityModule } from './entities/entity.module';
import { BildwerkQrAppRedirectModule } from './redirect/redirect.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import { QrDataDisplayModule } from 'app/qr-data-display/qr-data-display.module';

@NgModule({
  imports: [
    BrowserModule,
    BildwerkQrSharedModule,
    BildwerkQrCoreModule,
    BildwerkQrHomeModule,
    BildwerkQrAppRedirectModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    BildwerkQrEntityModule,
    BildwerkQrAppRoutingModule,
    QrDataDisplayModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent],
})
export class BildwerkQrAppModule {}
