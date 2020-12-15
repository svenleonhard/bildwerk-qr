import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserQrCodeExposed } from 'app/shared/model/user-qr-code-exposed.model';
import { UserQrCodeExposedService } from './user-qr-code-exposed.service';
import { UserQrCodeExposedDeleteDialogComponent } from './user-qr-code-exposed-delete-dialog.component';

@Component({
  selector: 'jhi-user-qr-code-exposed',
  templateUrl: './user-qr-code-exposed.component.html',
})
export class UserQrCodeExposedComponent implements OnInit, OnDestroy {
  userQrCodeExposeds?: IUserQrCodeExposed[];
  eventSubscriber?: Subscription;

  constructor(
    protected userQrCodeExposedService: UserQrCodeExposedService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.userQrCodeExposedService
      .query()
      .subscribe((res: HttpResponse<IUserQrCodeExposed[]>) => (this.userQrCodeExposeds = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInUserQrCodeExposeds();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IUserQrCodeExposed): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInUserQrCodeExposeds(): void {
    this.eventSubscriber = this.eventManager.subscribe('userQrCodeExposedListModification', () => this.loadAll());
  }

  delete(userQrCodeExposed: IUserQrCodeExposed): void {
    const modalRef = this.modalService.open(UserQrCodeExposedDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.userQrCodeExposed = userQrCodeExposed;
  }
}
