import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IQrRoute, QrRoute } from 'app/shared/model/qr-route.model';
import { QrRouteService } from './qr-route.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-qr-route-update',
  templateUrl: './qr-route-update.component.html',
})
export class QrRouteUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  startDateDp: any;
  endDateDp: any;

  editForm = this.fb.group({
    id: [],
    description: [],
    code: [],
    url: [],
    enabled: [],
    startDate: [],
    endDate: [],
    user: [],
  });

  constructor(
    protected qrRouteService: QrRouteService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ qrRoute }) => {
      this.updateForm(qrRoute);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(qrRoute: IQrRoute): void {
    this.editForm.patchValue({
      id: qrRoute.id,
      description: qrRoute.description,
      code: qrRoute.code,
      url: qrRoute.url,
      enabled: qrRoute.enabled,
      startDate: qrRoute.startDate,
      endDate: qrRoute.endDate,
      user: qrRoute.user,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const qrRoute = this.createFromForm();
    if (qrRoute.id !== undefined) {
      this.subscribeToSaveResponse(this.qrRouteService.update(qrRoute));
    } else {
      this.subscribeToSaveResponse(this.qrRouteService.create(qrRoute));
    }
  }

  private createFromForm(): IQrRoute {
    return {
      ...new QrRoute(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      code: this.editForm.get(['code'])!.value,
      url: this.editForm.get(['url'])!.value,
      enabled: this.editForm.get(['enabled'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQrRoute>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
