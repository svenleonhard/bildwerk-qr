import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserQrCode } from 'app/shared/model/user-qr-code.model';

@Component({
  selector: 'jhi-user-qr-code-detail',
  templateUrl: './user-qr-code-detail.component.html',
})
export class UserQrCodeDetailComponent implements OnInit {
  userQrCode: IUserQrCode | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userQrCode }) => (this.userQrCode = userQrCode));
  }

  previousState(): void {
    window.history.back();
  }
}
