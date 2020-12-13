import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IQrRoute } from 'app/shared/model/qr-route.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { QrRouteService } from './qr-route.service';
import { QrRouteDeleteDialogComponent } from './qr-route-delete-dialog.component';

@Component({
  selector: 'jhi-qr-route',
  templateUrl: './qr-route.component.html',
})
export class QrRouteComponent implements OnInit, OnDestroy {
  qrRoutes: IQrRoute[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected qrRouteService: QrRouteService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.qrRoutes = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.qrRouteService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IQrRoute[]>) => this.paginateQrRoutes(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.qrRoutes = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInQrRoutes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IQrRoute): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInQrRoutes(): void {
    this.eventSubscriber = this.eventManager.subscribe('qrRouteListModification', () => this.reset());
  }

  delete(qrRoute: IQrRoute): void {
    const modalRef = this.modalService.open(QrRouteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.qrRoute = qrRoute;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateQrRoutes(data: IQrRoute[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.qrRoutes.push(data[i]);
      }
    }
  }
}
