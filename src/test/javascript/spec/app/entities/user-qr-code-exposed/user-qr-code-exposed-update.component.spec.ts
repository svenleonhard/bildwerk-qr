import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BildwerkQrTestModule } from '../../../test.module';
import { UserQrCodeExposedUpdateComponent } from 'app/entities/user-qr-code-exposed/user-qr-code-exposed-update.component';
import { UserQrCodeExposedService } from 'app/entities/user-qr-code-exposed/user-qr-code-exposed.service';
import { UserQrCodeExposed } from 'app/shared/model/user-qr-code-exposed.model';

describe('Component Tests', () => {
  describe('UserQrCodeExposed Management Update Component', () => {
    let comp: UserQrCodeExposedUpdateComponent;
    let fixture: ComponentFixture<UserQrCodeExposedUpdateComponent>;
    let service: UserQrCodeExposedService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BildwerkQrTestModule],
        declarations: [UserQrCodeExposedUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(UserQrCodeExposedUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserQrCodeExposedUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserQrCodeExposedService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new UserQrCodeExposed(123);
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
        const entity = new UserQrCodeExposed();
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
