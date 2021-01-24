import { Component, OnInit } from '@angular/core';
import { NgxQrcodeElementTypes, NgxQrcodeErrorCorrectionLevels } from '@techiediaries/ngx-qrcode';
import { UserQrCodeService } from 'app/entities/user-qr-code/user-qr-code.service';
import { QrRouteService } from 'app/entities/qr-route/qr-route.service';
import { QrRoute } from 'app/shared/model/qr-route.model';
import { NgForm } from '@angular/forms';
import QRCodeStyling from 'qr-code-styling';

@Component({
  selector: 'jhi-qr-data-display',
  templateUrl: './qr-data-display.component.html',
  styleUrls: ['./qr-data-display.component.scss'],
})
export class QrDataDisplayComponent implements OnInit {
  elementType = NgxQrcodeElementTypes.URL;
  errorCorrectionLevel = NgxQrcodeErrorCorrectionLevels.MEDIUM;
  margin = 0;
  scale = 11;
  redirectUrl = '';
  currentRedirection = this.redirectUrl;
  currentQrCode: QrRoute | null = null;
  qrCodeImage = '';

  urlPattern = '(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?';

  updateRedirect: any;

  qrCode: QRCodeStyling | null = null;

  htmlElement: HTMLElement | undefined;

  constructor(private userQrCodeService: UserQrCodeService, private qrRouteService: QrRouteService) {}

  ngOnInit(): void {
    this.userQrCodeService.query().subscribe(res => {
      if (res.body) {
        const userQrCode = res.body.pop();
        if (userQrCode && userQrCode.code) {
          this.redirectUrl = location.origin + '/redirect/' + userQrCode.code;
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
    this.qrCode = this.getQrCode();
    this.qrCode.append(this.getHtmlElement());
  }

  getHtmlElement(): HTMLElement | undefined {
    const htmlElement = document.getElementById('canvas');
    if (htmlElement != null) {
      return htmlElement;
    }
    return undefined;
  }

  getQrCode(): QRCodeStyling {
    return new QRCodeStyling({
      width: 400,
      height: 400,
      data: 'https://www.bildwerk-medien.de/',
      dotsOptions: {
        color: '#000000',
        type: 'rounded',
      },
      cornersSquareOptions: {
        type: 'extra-rounded',
      },
      backgroundOptions: {
        color: '#FFFFFF',
      },
      imageOptions: {
        crossOrigin: 'anonymous',
        margin: 20,
      },
    });
  }

  updateRedirection(f: NgForm): void {
    if (f.valid) {
      if (this.updateRedirect && this.currentQrCode) {
        this.currentQrCode.url = this.updateRedirect;
        this.qrRouteService.update(this.currentQrCode).subscribe(res => {
          if (res.body) {
            this.currentQrCode = res.body;
            if (this.currentQrCode && this.currentQrCode.url) {
              this.currentRedirection = this.currentQrCode.url;
            }
          }
        });
      }
    }
  }

  onDownload(): void {
    this.qrCodeImage = document.getElementsByTagName('img')[0].src;
  }
}
