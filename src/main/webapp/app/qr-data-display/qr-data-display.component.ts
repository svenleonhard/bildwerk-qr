import { Component, OnInit } from '@angular/core';
import { NgxQrcodeElementTypes, NgxQrcodeErrorCorrectionLevels } from '@techiediaries/ngx-qrcode';
import { UserQrCodeService } from 'app/entities/user-qr-code/user-qr-code.service';
import { QrRouteService } from 'app/entities/qr-route/qr-route.service';
import { QrRoute } from 'app/shared/model/qr-route.model';
import { BASE_REDIRECTION_URL } from 'app/app.constants';

@Component({
  selector: 'jhi-qr-data-display',
  templateUrl: './qr-data-display.component.html',
  styleUrls: ['./qr-data-display.component.scss'],
})
export class QrDataDisplayComponent implements OnInit {
  elementType = NgxQrcodeElementTypes.URL;
  errorCorrectionLevel = NgxQrcodeErrorCorrectionLevels.MEDIUM;
  value = 'https://google.de';
  margin = 0;
  scale = 11;
  redirectUrl = '';
  currentRedirection = this.redirectUrl;
  currentQrCode: QrRoute | null = null;

  constructor(private userQrCodeService: UserQrCodeService, private qrRouteService: QrRouteService) {}

  ngOnInit(): void {
    this.userQrCodeService.query().subscribe(res => {
      if (res.body) {
        const userQrCode = res.body.pop();
        if (userQrCode && userQrCode.code) {
          this.redirectUrl = BASE_REDIRECTION_URL + '/redirect/' + userQrCode.code;
        }
      }
    });

    this.qrRouteService.query({ 'enabled.equals': true }).subscribe(res => {
      if (res.body) {
        const qrRoute = res.body.pop();
        if (qrRoute && qrRoute.url) {
          this.currentRedirection = qrRoute.url;
          this.currentQrCode = qrRoute;
        }
      }
    });
  }

  // updateRedirection() {}
}
