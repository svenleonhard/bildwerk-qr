import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserQrCode } from 'app/shared/model/user-qr-code.model';
import { UserQrCodeService } from './user-qr-code.service';
import { UserQrCodeDeleteDialogComponent } from './user-qr-code-delete-dialog.component';

@Component({
  selector: 'jhi-user-qr-code',
  templateUrl: './user-qr-code.component.html',
})
export class UserQrCodeComponent implements OnInit, OnDestroy {
  userQrCodes?: IUserQrCode[];
  eventSubscriber?: Subscription;

  constructor(protected userQrCodeService: UserQrCodeService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.userQrCodeService.query().subscribe((res: HttpResponse<IUserQrCode[]>) => (this.userQrCodes = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInUserQrCodes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IUserQrCode): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInUserQrCodes(): void {
    this.eventSubscriber = this.eventManager.subscribe('userQrCodeListModification', () => this.loadAll());
  }

  delete(userQrCode: IUserQrCode): void {
    const modalRef = this.modalService.open(UserQrCodeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.userQrCode = userQrCode;
  }
}
