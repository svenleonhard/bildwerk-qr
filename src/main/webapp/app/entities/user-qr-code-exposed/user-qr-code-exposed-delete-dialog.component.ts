import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserQrCodeExposed } from 'app/shared/model/user-qr-code-exposed.model';
import { UserQrCodeExposedService } from './user-qr-code-exposed.service';

@Component({
  templateUrl: './user-qr-code-exposed-delete-dialog.component.html',
})
export class UserQrCodeExposedDeleteDialogComponent {
  userQrCodeExposed?: IUserQrCodeExposed;

  constructor(
    protected userQrCodeExposedService: UserQrCodeExposedService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userQrCodeExposedService.delete(id).subscribe(() => {
      this.eventManager.broadcast('userQrCodeExposedListModification');
      this.activeModal.close();
    });
  }
}
