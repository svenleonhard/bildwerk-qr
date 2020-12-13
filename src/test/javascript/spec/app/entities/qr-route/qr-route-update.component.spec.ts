import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BildwerkQrTestModule } from '../../../test.module';
import { QrRouteUpdateComponent } from 'app/entities/qr-route/qr-route-update.component';
import { QrRouteService } from 'app/entities/qr-route/qr-route.service';
import { QrRoute } from 'app/shared/model/qr-route.model';

describe('Component Tests', () => {
  describe('QrRoute Management Update Component', () => {
    let comp: QrRouteUpdateComponent;
    let fixture: ComponentFixture<QrRouteUpdateComponent>;
    let service: QrRouteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BildwerkQrTestModule],
        declarations: [QrRouteUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(QrRouteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QrRouteUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(QrRouteService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new QrRoute(123);
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
        const entity = new QrRoute();
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
