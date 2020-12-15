import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IUserQrCodeExposed, UserQrCodeExposed } from 'app/shared/model/user-qr-code-exposed.model';
import { UserQrCodeExposedService } from './user-qr-code-exposed.service';

@Component({
  selector: 'jhi-user-qr-code-exposed-update',
  templateUrl: './user-qr-code-exposed-update.component.html',
})
export class UserQrCodeExposedUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    code: [],
  });

  constructor(
    protected userQrCodeExposedService: UserQrCodeExposedService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userQrCodeExposed }) => {
      this.updateForm(userQrCodeExposed);
    });
  }

  updateForm(userQrCodeExposed: IUserQrCodeExposed): void {
    this.editForm.patchValue({
      id: userQrCodeExposed.id,
      code: userQrCodeExposed.code,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userQrCodeExposed = this.createFromForm();
    if (userQrCodeExposed.id !== undefined) {
      this.subscribeToSaveResponse(this.userQrCodeExposedService.update(userQrCodeExposed));
    } else {
      this.subscribeToSaveResponse(this.userQrCodeExposedService.create(userQrCodeExposed));
    }
  }

  private createFromForm(): IUserQrCodeExposed {
    return {
      ...new UserQrCodeExposed(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserQrCodeExposed>>): void {
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
}
