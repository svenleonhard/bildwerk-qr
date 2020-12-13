import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BildwerkQrTestModule } from '../../../test.module';
import { UserQrCodeUpdateComponent } from 'app/entities/user-qr-code/user-qr-code-update.component';
import { UserQrCodeService } from 'app/entities/user-qr-code/user-qr-code.service';
import { UserQrCode } from 'app/shared/model/user-qr-code.model';

describe('Component Tests', () => {
  describe('UserQrCode Management Update Component', () => {
    let comp: UserQrCodeUpdateComponent;
    let fixture: ComponentFixture<UserQrCodeUpdateComponent>;
    let service: UserQrCodeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BildwerkQrTestModule],
        declarations: [UserQrCodeUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(UserQrCodeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserQrCodeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserQrCodeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new UserQrCode(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new UserQrCode();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
