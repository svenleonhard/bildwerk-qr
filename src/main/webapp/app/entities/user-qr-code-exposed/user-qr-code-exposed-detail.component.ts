import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserQrCodeExposed } from 'app/shared/model/user-qr-code-exposed.model';

@Component({
  selector: 'jhi-user-qr-code-exposed-detail',
  templateUrl: './user-qr-code-exposed-detail.component.html',
})
export class UserQrCodeExposedDetailComponent implements OnInit {
  userQrCodeExposed: IUserQrCodeExposed | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userQrCodeExposed }) => (this.userQrCodeExposed = userQrCodeExposed));
  }

  previousState(): void {
    window.history.back();
  }
}
