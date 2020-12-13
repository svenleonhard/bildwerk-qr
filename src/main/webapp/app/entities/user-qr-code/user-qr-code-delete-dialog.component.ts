import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserQrCode } from 'app/shared/model/user-qr-code.model';
import { UserQrCodeService } from './user-qr-code.service';

@Component({
  templateUrl: './user-qr-code-delete-dialog.component.html',
})
export class UserQrCodeDeleteDialogComponent {
  userQrCode?: IUserQrCode;

  constructor(
    protected userQrCodeService: UserQrCodeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userQrCodeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('userQrCodeListModification');
      this.activeModal.close();
    });
  }
}
