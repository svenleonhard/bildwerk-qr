import { Component, OnInit } from '@angular/core';
import { NgxQrcodeElementTypes, NgxQrcodeErrorCorrectionLevels } from '@techiediaries/ngx-qrcode';

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
  scale = 10;

  constructor() {}

  ngOnInit(): void {}
}
