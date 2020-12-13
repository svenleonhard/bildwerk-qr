import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IUserQrCode, UserQrCode } from 'app/shared/model/user-qr-code.model';
import { UserQrCodeService } from './user-qr-code.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-user-qr-code-update',
  templateUrl: './user-qr-code-update.component.html',
})
export class UserQrCodeUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    code: [],
    user: [],
  });

  constructor(
    protected userQrCodeService: UserQrCodeService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userQrCode }) => {
      this.updateForm(userQrCode);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(userQrCode: IUserQrCode): void {
    this.editForm.patchValue({
      id: userQrCode.id,
      code: userQrCode.code,
      user: userQrCode.user,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userQrCode = this.createFromForm();
    if (userQrCode.id !== undefined) {
      this.subscribeToSaveResponse(this.userQrCodeService.update(userQrCode));
    } else {
      this.subscribeToSaveResponse(this.userQrCodeService.create(userQrCode));
    }
  }

  private createFromForm(): IUserQrCode {
    return {
      ...new UserQrCode(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserQrCode>>): void {
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
