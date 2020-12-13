import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQrRoute } from 'app/shared/model/qr-route.model';

@Component({
  selector: 'jhi-qr-route-detail',
  templateUrl: './qr-route-detail.component.html',
})
export class QrRouteDetailComponent implements OnInit {
  qrRoute: IQrRoute | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ qrRoute }) => (this.qrRoute = qrRoute));
  }

  previousState(): void {
    window.history.back();
  }
}
