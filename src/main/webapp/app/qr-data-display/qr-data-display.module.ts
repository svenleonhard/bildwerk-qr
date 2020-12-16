import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { QrDataDisplayComponent } from 'app/qr-data-display/qr-data-display.component';
import { NgxQRCodeModule } from '@techiediaries/ngx-qrcode';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [QrDataDisplayComponent],
  exports: [QrDataDisplayComponent],
  imports: [CommonModule, NgxQRCodeModule, RouterModule],
})
export class QrDataDisplayModule {}
