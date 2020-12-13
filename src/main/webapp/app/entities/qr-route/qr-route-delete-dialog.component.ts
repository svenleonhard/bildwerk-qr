import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IQrRoute } from 'app/shared/model/qr-route.model';
import { QrRouteService } from './qr-route.service';

@Component({
  templateUrl: './qr-route-delete-dialog.component.html',
})
export class QrRouteDeleteDialogComponent {
  qrRoute?: IQrRoute;

  constructor(protected qrRouteService: QrRouteService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.qrRouteService.delete(id).subscribe(() => {
      this.eventManager.broadcast('qrRouteListModification');
      this.activeModal.close();
    });
  }
}
